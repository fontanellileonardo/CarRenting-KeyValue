import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.UnresolvedConflictException;
import com.basho.riak.client.api.commands.datatypes.CounterUpdate;
import com.basho.riak.client.api.commands.datatypes.FetchCounter;
import com.basho.riak.client.api.commands.datatypes.UpdateCounter;
import com.basho.riak.client.api.commands.indexes.BinIndexQuery;
import com.basho.riak.client.api.commands.indexes.IntIndexQuery;
import com.basho.riak.client.api.commands.indexes.SecondaryIndexQuery.Response.Entry;
import com.basho.riak.client.api.commands.kv.DeleteValue;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.query.crdt.types.RiakCounter;
import com.basho.riak.client.core.query.indexes.LongIntIndex;
import com.basho.riak.client.core.query.indexes.StringBinIndex;
import com.basho.riak.client.core.util.BinaryValue;

public class RiakHandleDB {

    private static RiakCluster cluster;
    private static RiakClient client;
    private static Namespace carRentingBucket;
    private static Namespace indexBucket;
    private static Namespace counterBucket;
    private static Location counterLoc;
    private static CounterUpdate cu;
    private static UpdateCounter updateCounter;
	
    /*
	static {
		
		cluster = setUpCluster();
		client = new RiakClient(cluster);
		carRentingBucket = new Namespace("CarRenting");
		indexBucket = new Namespace("IndexBucket");
		counterBucket = new Namespace("Counters", "Counters");
		counterLoc = new Location(counterBucket,"counterKey");
		// [DEBUG] Vedere se va bene
		cu = new CounterUpdate(1);
		updateCounter = new UpdateCounter.Builder(counterLoc,cu).build();
		// Fine
		
	}
	*/
    
    // Open the connection with the DB
	public static void openConnection() {
		cluster = DBConnection.getInstance().cluster;
		client = new RiakClient(cluster);
		carRentingBucket = new Namespace("CarRenting");
		indexBucket = new Namespace("IndexBucket");
		counterBucket = new Namespace("Counters", "Counters");
		counterLoc = new Location(counterBucket,"counterKey");
		cu = new CounterUpdate(1);
		updateCounter = new UpdateCounter.Builder(counterLoc,cu).build();
	}
	/*
    private static RiakCluster setUpCluster() {
        // This example will use only one node listening on localhost:10017
        RiakNode node = new RiakNode.Builder()
                .withRemoteAddress("127.0.0.1")
                .withRemotePort(8087)
                .build();

        // This cluster object takes our one node as an argument
        RiakCluster cluster = new RiakCluster.Builder(node)
                .build();

        // The cluster must be started to work, otherwise you will see errors
        cluster.start();

        return cluster;
    }
    */
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
    
 // Create the index associated with the Feedback
    private static Boolean createIndex(long mark, String key) {
    	RiakObject feedInd = new RiakObject().setContentType("text/plain").setValue(BinaryValue.create("-"));
		Location feedIndLocation = new Location(indexBucket, key);
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
   
    public static int create(FeedbackKV feedback,String fiscalCode) {
    	try {
    		client.execute(updateCounter);
    		long counter = getCounter();
    		String key = "feedb:" + counter + ":" + fiscalCode;
    		if (counter == -1 || createIndex(feedback.getMark(), key) == false)
    			return 2;
    		Location feedLoc = new Location(carRentingBucket, key);
    		StoreValue storeOp = new StoreValue.Builder(feedback).withLocation(feedLoc).build();
			client.execute(storeOp);
    	} catch (Exception ex) {
    		System.err.println(ex.getMessage());
    		return 2;
    	}
    	return 0;
    }
    
    public static HashMap<String, Object> selectAllFeedbacks(Integer mark){
    	IntIndexQuery biq;
    	IntIndexQuery.Response response;
		FetchValue fetchFeedback; 
		List<Entry<Long>> entries;
		HashMap<String, Object> results = new HashMap<String, Object>();
		String key;
		FeedbackKV fetchedFeedback;
    	try {
	    	biq = new IntIndexQuery.Builder(indexBucket, "Mark", Utils.MIN_MARK, (long)mark).build();
			response = client.execute(biq);
			entries  = response.getEntries();
			int i =0;
			for (IntIndexQuery.Response.Entry entry : entries) {
				key = entry.getRiakObjectLocation().getKey().toString();
				String[] splitKey = key.split(":");
				fetchFeedback = new FetchValue.Builder(new Location(carRentingBucket, entry.getRiakObjectLocation().getKey())).build();     
		        fetchedFeedback = client.execute(fetchFeedback).getValue(FeedbackKV.class);
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
    
    public static void finish() {
    	cluster.shutdown();
    }
  /*  
    public static void main(String[]args) {
    	
    	/*
    	DeleteValue deleteOp1 = new DeleteValue.Builder(new Location(indexBucket, "feedback:01:CODFISC1")).build();
    	DeleteValue deleteOp2 = new DeleteValue.Builder(new Location(indexBucket, "feedback:02:CODFISC2")).build();
    	try {
    		client.execute(deleteOp1);
    		client.execute(deleteOp2);
    	} catch (Exception e) {
    		System.err.println(e.getMessage());
    	}
    	/*
    	for(int i = 0; i < 5; i++) {
    		System.out.println("RETURN CREATE: " + create(new FeedbackKV(i+1,"",Utils.getCurrentSqlDate()),"COD FISCALE"));
    	}
    	HashMap<String, Object> result = selectAllFeedbacks(5);
    	System.out.println(result.size());
    	for(int i = 0; i < result.size()/2; i++) {
    		System.out.println("FiscalCode: " + result.get("FiscalCode:"+i));
    		System.out.println("VOTO: " + ((FeedbackKV) result.get("Feedback:"+i)).getMark());
    		System.out.println("DATA: " + ((FeedbackKV) result.get("Feedback:"+i)).getDate());
    	}
    	finish();
    }*/

}
