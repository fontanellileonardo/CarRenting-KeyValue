import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.UnresolvedConflictException;
import com.basho.riak.client.api.commands.datatypes.CounterUpdate;
import com.basho.riak.client.api.commands.datatypes.FetchCounter;
import com.basho.riak.client.api.commands.datatypes.UpdateCounter;
import com.basho.riak.client.api.commands.indexes.IntIndexQuery;
import com.basho.riak.client.api.commands.indexes.SecondaryIndexQuery.Response.Entry;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.query.crdt.types.RiakCounter;
import com.basho.riak.client.core.query.indexes.LongIntIndex;
import com.basho.riak.client.core.util.BinaryValue;

public class RiakHandleDB {

    private static RiakCluster cluster;
    private static RiakClient client;
    private static Namespace carRentingBucket;
    private static Namespace counterBucket;
    private static Location counterLoc;
    private static CounterUpdate cu;
    private static UpdateCounter updateCounter;
    
    // Open the connection with the DB
	public static void openConnection() {
		cluster = DBConnection.getInstance().cluster;
		client = new RiakClient(cluster);
		carRentingBucket = new Namespace("CarRenting");
		counterBucket = new Namespace("Counters", "Counters");
		counterLoc = new Location(counterBucket,"counterKey");
		cu = new CounterUpdate(1);
		updateCounter = new UpdateCounter.Builder(counterLoc,cu).build();
	}

	// Retrieve the Counter
    private static long getCounter() {
    	FetchCounter fetch = new FetchCounter.Builder(counterLoc).build();
    	try {
			FetchCounter.Response response = client.execute(fetch);
			RiakCounter counter = response.getDatatype();
			return counter.view();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return -1;
    }
    
    // Insert a new value in the KV with its index (value of feedback's mark)
    private static Boolean create(String key, String value, long mark) {
    	RiakObject feedInd = new RiakObject().setContentType("text/plain").setValue(BinaryValue.create(value));
		Location feedIndLocation = new Location(carRentingBucket, key);
		feedInd.getIndexes().getIndex(LongIntIndex.named("Mark")).add(mark);
		StoreValue storeOp = new StoreValue.Builder(feedInd).withLocation(feedIndLocation).build();
		try {
			client.execute(storeOp);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
    }
    
    // Create a new value in the KV
    private static Boolean create(String key, String value) {
    	if(value.equals(""))
    		value = " ";
    	RiakObject feedInd = new RiakObject().setContentType("text/plain").setValue(BinaryValue.create(value));
		Location feedIndLocation = new Location(carRentingBucket, key);
		StoreValue storeOp = new StoreValue.Builder(feedInd).withLocation(feedIndLocation).build();
		try {
			client.execute(storeOp);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
    }
    
    // Create the FeedbackKV's attributes in the KV DB
    public static int create(FeedbackKV feedback,String fiscalCode) {
    	try {
    		client.execute(updateCounter);
    		long counter = getCounter();
    		if (counter == -1 )	// error in retrieving the counter
    			return 2;
    		String key = "feedb:" + counter + ":" + fiscalCode;
    		// Create mark and its index, comment and date
    		if(create(key+":mark",feedback.getMark().toString(),feedback.getMark()) && 
    				create(key+":comment",feedback.getComment()) &&
    				create(key+":date",feedback.getDate().toString())) {
    			return 0;
    		} else 
    			return 2;
    	} catch (Exception ex) {
    		System.err.println(ex.getMessage());
    		return 2;
    	}
    }

    // Read the FeedbackKV's attribute from DB
    private static String readAttribute(String key) throws UnresolvedConflictException,ExecutionException,InterruptedException{
    	FetchValue fetchValue; 
		RiakObject riakObj = null;
		
		fetchValue = new FetchValue.Builder(new Location(carRentingBucket, key)).build();     
		riakObj = client.execute(fetchValue).getValue(RiakObject.class);
		if(riakObj != null)
			return riakObj.getValue().toString();
		return null;
    }
    
    // Retrieve the FeedbackKV
    private static FeedbackKV readFeedbackKV(String key) throws UnresolvedConflictException,ExecutionException,InterruptedException{
    	FeedbackKV feedbackKV = new FeedbackKV();
    	feedbackKV.setMark(Integer.parseInt(readAttribute(key+":mark")));
    	feedbackKV.setDate(Date.valueOf(readAttribute(key+":date")));
    	feedbackKV.setComment(readAttribute(key+":comment"));
    	return feedbackKV;
    }
    
    // Get all the feedback according to their mark (if mark = 3 -> all feedback with mark = 1,2, and 3)
    // The HashMap is structured as: <FiscalCode:0,fiscalCode_value>,<Feedback:0,feddbackKV_obj>
    public static HashMap<String, Object> selectAllFeedbacks(Integer mark){
    	
    	IntIndexQuery biq;
    	IntIndexQuery.Response response;
		List<Entry<Long>> entries;
		HashMap<String, Object> results = new HashMap<String, Object>();
		String key;
		FeedbackKV fetchedFeedback;
    	try {
	    	biq = new IntIndexQuery.Builder(carRentingBucket, "Mark", Utils.MIN_MARK, (long)mark).build();
			response = client.execute(biq);
			// Get all the keys from the index
			entries  = response.getEntries();
			int i =0;
			for (IntIndexQuery.Response.Entry entry : entries) {	// key with ":mark"
				key = entry.getRiakObjectLocation().getKey().toString();
				String[] splitKey = key.split(":");
				key = splitKey[0]+ ":" + splitKey[1] + ":" + splitKey[2];	// key without ":mark"   
		        fetchedFeedback = readFeedbackKV(key);
		        results.put("FiscalCode:"+i, splitKey[2]);
		        results.put("Feedback:"+i, fetchedFeedback);
		        i++;
			}
			
		} catch (UnresolvedConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	return results;
    }
    
    // Close the connection with the DB
    public static void finish() {
    	if(cluster != null)
    		cluster.shutdown();
    }
}
