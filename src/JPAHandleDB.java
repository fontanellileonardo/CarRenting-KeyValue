import javax.persistence.*;
import org.hibernate.exception.ConstraintViolationException;

public class JPAHandleDB {
	private static EntityManagerFactory factory;
	private static EntityManager entityManager;
	
	static {
		factory = Persistence.createEntityManagerFactory("CarRenting");
	}
	
	public static int create(Object o) {
		System.out.println("Add a new object");
		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(o);
			entityManager.getTransaction().commit();
			System.out.println("New object added");
		} catch (Exception ex) {
			if((ex.getCause() instanceof PersistenceException) && (ex.getCause().getCause() instanceof ConstraintViolationException)) {
				ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getCause().getCause();
				if(constraintViolationException.getErrorCode() == 1062) { // Object already exists
					System.err.println("Object already exists");
					return 1;
				}
			}
			return 2;
		} finally {
			entityManager.close();
		}
		return 0;
	}
	
	public static <T> Object read(Class<T> entity, Object id) {
		System.out.println("Getting a new object");
		Object o = null;
		try {
			entityManager = factory.createEntityManager();
			o = entityManager.find(entity, id);
		} catch (Exception ex) {
			System.err.println("Exception during read: " + ex.getMessage());
		} finally {
			entityManager.close();
		}
		return o;
	}
	
	public static boolean update(Object o) {
		System.out.println("Update an object");
		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.merge(o);
			entityManager.getTransaction().commit();
		} catch(Exception ex) {
			System.err.println("Exception during update: " + ex.getMessage());
			return false;
		} finally {
			entityManager.close();
		}
		return true;
	}
	
	public static <T> boolean delete(Class<T> entity, Object id) {
		System.out.println("Delete an object");
		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			Object o = entityManager.getReference(entity, id);
			entityManager.remove(o);
			entityManager.getTransaction().commit();
		} catch(Exception ex) {
			System.err.println("Exception during delete: " + ex.getMessage());
			return false;
		} finally {
			entityManager.close();
		}
		return true;
	}
	
	public static void finish() {
		factory.close();
	}
}
