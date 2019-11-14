import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.cap.UnresolvedConflictException;
import com.basho.riak.client.api.commands.datatypes.CounterUpdate;
import com.basho.riak.client.api.commands.datatypes.FetchCounter;
import com.basho.riak.client.api.commands.datatypes.UpdateCounter;
import com.basho.riak.client.api.commands.indexes.BinIndexQuery;
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
	
	static {
		try {
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
		} catch(UnknownHostException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
    private static RiakCluster setUpCluster() throws UnknownHostException {
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
    private static Boolean createIndex(String mark, String key) {
    	RiakObject feedInd = new RiakObject().setContentType("text/plain").setValue(BinaryValue.create("-"));
		Location feedIndLocation = new Location(indexBucket, key);
		feedInd.getIndexes().getIndex(StringBinIndex.named("Mark")).add(mark);
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
   
    private static int create(FeedbackKV feedback,String fiscalCode) {
    	try {
    		client.execute(updateCounter);
    		long counter = getCounter();
    		String key = "feedb:" + counter + ":" + fiscalCode;
    		if (counter == -1 || createIndex(feedback.getMark().toString(), key) == false)
    			return 2;
    		Location feedLoc = new Location(carRentingBucket, key);
    		StoreValue storeOp = new StoreValue.Builder(feedback).withLocation(feedLoc).build();
			client.execute(storeOp);
    	} catch (Exception ex) {
    		System.err.println(ex.getMessage());
    	}
    	return 0;
    }
    
	public static List<FeedbackKV> selectAllFeedbacks() {
		int maxMark = 5;
		return selectAllFeedbacks(maxMark);
	}
    
    private static List<FeedbackKV> selectAllFeedbacks(Integer mark){
		BinIndexQuery biq;
		BinIndexQuery.Response response;
		FetchValue fetchFeedback; 
		List<Entry<String>> entries;
		List<FeedbackKV> results = new ArrayList<>();
    	try {
			for(Integer i = mark; i>0; i--) {
		    	biq = new BinIndexQuery.Builder(indexBucket, "Mark", i.toString()).build();
				response = client.execute(biq);
				entries  = response.getEntries();
				for (BinIndexQuery.Response.Entry entry : entries) {
					System.out.println(entry.getRiakObjectLocation().getKey());
					fetchFeedback = new FetchValue.Builder(new Location(carRentingBucket, entry.getRiakObjectLocation().getKey())).build();
			        FeedbackKV fetchedFeedback;
			        fetchedFeedback = client.execute(fetchFeedback).getValue(FeedbackKV.class);
			        results.add(fetchedFeedback);
				}
			}
		} catch (UnresolvedConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
    	return results;
    }
    
    private static void finish() {
    	cluster.shutdown();
    }
    
    public static void main(String[]args) {
    	
    	/*
    	DeleteValue deleteOp1 = new DeleteValue.Builder(new Location(indexBucket, "feedback:01:CODFISC1")).build();
    	DeleteValue deleteOp2 = new DeleteValue.Builder(new Location(indexBucket, "feedback:02:CODFISC2")).build();
    	try {
    		client.execute(deleteOp1);
    		client.execute(deleteOp2);
    	} catch (Exception e) {
    		System.err.println(e.getMessage());
    	}*/
    	
    	for(int i = 0; i < 5; i++) {
    		System.out.println("RETURN CREATE: " + create(new FeedbackKV(i+1,"",Utils.getCurrentSqlDate()),"COD FISCALE"));
    	}
    	List<FeedbackKV> result = selectAllFeedbacks(5);
    	for(int i = 0; i<result.size(); i++) {
    		System.out.println("VOTO: " + result.get(i).getMark());
    		System.out.println("DATA: " + result.get(i).getDate());
    	}
    	finish();
    }

}
