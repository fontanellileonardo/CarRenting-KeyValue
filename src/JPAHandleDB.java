import java.util.List;

import javax.persistence.*;
import org.hibernate.exception.ConstraintViolationException;

public class JPAHandleDB {
	private static EntityManagerFactory factory;
	private static EntityManager entityManager;
	private static String selectAllCustomers = "SELECT u FROM User u WHERE u.customer = true";
	private static String selectAllFeedbacks = "SELECT f FROM Feedback f WHERE f.mark <= :minMark";
	
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
			System.err.println("Exception during create: " + ex.getMessage());
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
	
	public static List<User> selectAllCustomers() {
		List<User> result = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<User> query = entityManager.createQuery(selectAllCustomers, User.class);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during customers selection: " + ex.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
		return result;
	}
	
	public static List<Feedback> selectAllFeedbacks() {
		List<Feedback> result = null;
		int maxMark = 5;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Feedback> query = entityManager.createQuery(selectAllFeedbacks, Feedback.class);
			query.setParameter("minMark", maxMark);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during feedbacks selection: " + ex.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
		return result;
	}
	
	public static List<Feedback> selectAllFeedbacks(int minMark) {
		List<Feedback> result = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Feedback> query = entityManager.createQuery(selectAllFeedbacks, Feedback.class);
			query.setParameter("minMark", minMark);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during feedbacks selection: " + ex.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
		return result;
	}
	
	public static void finish() {
		factory.close();
	}
}
