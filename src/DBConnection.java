import java.net.UnknownHostException;
import java.util.logging.Level;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.service.spi.ServiceException;

import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;

// Open only one connection with the DB
public class DBConnection {
	private static DBConnection instance;
	public static EntityManagerFactory factory;
	public static RiakCluster cluster;
	
	private DBConnection() {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		try {
			factory = Persistence.createEntityManagerFactory("CarRenting");
			//Riak connection
			RiakNode node = new RiakNode.Builder()
	                .withRemoteAddress("127.0.0.1")
	                .withRemotePort(8087)
	                .build();
	        // This cluster object takes our one node as an argument
	        this.cluster = new RiakCluster.Builder(node)
	                .build();
	        // The cluster must be started to work, otherwise you will see errors
	        this.cluster.start();
		} catch (ServiceException ex) {
			System.err.println("Unable to establish a connection to MySQL database");
		} 
	}
	
	public static DBConnection getInstance() {
	    if(instance == null){
	        synchronized (DBConnection.class) {
	            if(instance == null){
	                instance = new DBConnection();
	            }
	        }
	    }
	    return instance;
	}
	
}
