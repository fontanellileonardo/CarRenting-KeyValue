import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import org.hibernate.exception.ConstraintViolationException;

public class JPAHandleDB {
	private static EntityManagerFactory factory;
	private static EntityManager entityManager;
	private static String selectAllCustomers = "SELECT u FROM User u WHERE u.customer = true";
	private static String selectAllFeedbacks = "SELECT f FROM Feedback f WHERE f.mark <= :minMark";
	private static String selectActiveReservation = "SELECT r FROM Reservation r WHERE r.User = :user AND r.pickUpDate > :pickUpDate";
	private static String findAvailableCars = "SELECT c FROM Car c WHERE c.location = :location AND c.seatNumber = :seatNumber AND c.idCar NOT IN "
												+ "(SELECT r.Car FROM Reservation r WHERE (r.pickUpDate BETWEEN :pickUpDate AND :deliveryDate) "
												+ "OR (r.deliveryDate BETWEEN :pickUpDate AND :deliveryDate) "
												+ "OR (pickUpDate < :pickUpDate AND deliveryDate > :deliveryDate))";
	
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
			if((ex instanceof PersistenceException) && (ex.getCause() instanceof ConstraintViolationException)) {
				ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getCause();
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
		int maxMark = 5;
		return selectAllFeedbacks(maxMark);
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
	
	public static List<Car> findAvailableCars(LocalDate arrival, LocalDate departure, String loc, String seats){
		List<Car> result = null;
		Date arr = java.sql.Date.valueOf(arrival);
		Date dep = java.sql.Date.valueOf(departure);
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Car> query = entityManager.createQuery(findAvailableCars, Car.class);
			query.setParameter("location", loc);
			query.setParameter("seatNumber", seats);
			query.setParameter("pickUpDate", arr);
			query.setParameter("deliveryDate", dep);
			query.setParameter("pickUpDate", arr);
			query.setParameter("deliveryDate", dep);
			query.setParameter("pickUpDate", arr);
			query.setParameter("deliveryDate", dep);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception while searching for available cars");
			return null;
		}
		finally {
			entityManager.close();
		}
		return result;
	}
	
	public static int selectActiveReservation(Reservation r) {
		List<Reservation> result = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectActiveReservation, Reservation.class);
			query.setParameter("user", r.getUser()); 
			query.setParameter("pickUpDate", r.getPickUpDate());
			result = query.getResultList();
		}
		catch (Exception ex){
			System.err.println("Exception during reservation selection: " + ex.getMessage());
			return 2;
		}
		finally {
			entityManager.close();
		}
		if(result == null)
			return 0;
		else
			return 1;
	}
	
	public static int insertNewReservation(Reservation r) { //Ho supposto che si passasse un oggetto Reservation a questo metodo. Se si vuole far passare gli stessi parametri della scorsa versione, si può anche cambiare (o fare un overloading)
		int reservation = selectActiveReservation(r);
		if (reservation == 1) {
			System.err.println("It's not permitted to book more than one car at a time");
			return 1;
		}
		int result = create(r);
		return result;
	}
	
	public static boolean insertNewFeedback(Feedback f) {
		int result = create(f);
		return result;
	}
	
	public static void finish() {
		factory.close();
	}
}
