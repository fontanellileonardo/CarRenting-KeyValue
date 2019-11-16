import java.sql.Date;
import java.util.List;

import javax.persistence.*;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.service.spi.ServiceException;

public class JPAHandleDB {
	private static EntityManagerFactory factory;
	private static EntityManager entityManager;
	private static String findUser = "SELECT u FROM User u WHERE u.email= :email AND u.password= :password AND u.customer= :customer ";
	private static String selectAllCustomers = "SELECT u FROM User u WHERE u.customer = true";
//	private static String selectAllFeedbacks = "SELECT f FROM Feedback f WHERE f.mark <= :minMark";
	private static String selectActiveReservation = "SELECT r FROM Reservation r WHERE r.user = :user AND r.pickUpDate > :actualDate";
	private static String findAvailableCars = "SELECT c FROM Car c WHERE c.location = :location AND c.seatNumber = :seatNumber AND c.removed = false AND c.licensePlate NOT IN "
												+ "(SELECT r.car FROM Reservation r WHERE (r.pickUpDate BETWEEN :pickUpDate AND :deliveryDate) "
												+ "OR (r.deliveryDate BETWEEN :pickUpDate AND :deliveryDate) "
												+ "OR (pickUpDate < :pickUpDate AND deliveryDate > :deliveryDate))";
	private static String selectAllReservations = "SELECT r FROM Reservation r";
	private static String selectCarReservations = "SELECT r FROM Reservation r WHERE r.car = :car";
	private static String selectAllCars = "SELECT c FROM Car c";
	private static String selectAllLicensePlates = "SELECT c.licensePlate FROM Car c";
	private static String selectCarActiveReservations = "SELECT r FROM Reservation r WHERE r.car = :car AND r.pickUpDate > :actualDate";
	private static String selectCustomerReservations = "SELECT r FROM Reservation r WHERE r.user = :user";

	static {
		try {
			factory = Persistence.createEntityManagerFactory("CarRenting");
		} catch (ServiceException ex) {
			System.err.println("Unable to establish a connection to MySQL database");
		}
	}
	
	/* Create new object. 
	 * Return 0 -> everything is ok
	 * Return 1 -> object already exists or a unique value already exists
	 * Return 2 -> DB error
	 */
	public static int create(Object o) {
		System.out.println("Add a new object");
		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(o);
			entityManager.getTransaction().commit();
			System.out.println("New object added");
		} 
		catch (RollbackException ex) {
		      if (ex.getCause() instanceof PersistenceException && ex.getCause().getCause() instanceof ConstraintViolationException) {
		    	  System.err.println("Object already exists");
		    	  return 1;
		    	
		      }
		} catch (PersistenceException ex) {
		      if (ex.getCause() instanceof ConstraintViolationException) {
		    	  System.err.println("UNIQUE parameter error");
		    	  return 1;
		      }
		}  catch (Exception ex) {
			System.err.println("Database Error");
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
	
	public static int logIn(User user) {
		int result = 1;
		User retrievedUser = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<User> query = entityManager.createQuery(findUser, User.class);
			query.setParameter("email", user.getEmail());
			query.setParameter("password", user.getPassword());
			query.setParameter("customer", user.getCustomer());
			retrievedUser = query.getSingleResult();
			user.setFiscalCode(retrievedUser.getFiscalCode());
			user.setNickName(retrievedUser.getNickName());
			user.setName(retrievedUser.getName());
			user.setSurname(retrievedUser.getSurname());
			System.out.println("Logged User: fc:"+user.getFiscalCode()+" nickName: "
			+user.getNickName()+" name:"+user.getName()+" surname:"+user.getSurname()+" customer: "+user.getCustomer()
			+" email: "+user.getEmail()+" password:"+user.getPassword());
			
			if(user != null)
				result = 0;
		} catch (NoResultException ex) {
			System.err.println("Database error while searching for user data: " + ex.getMessage());
			result = 1;
			return result;
		}catch (Exception ex) {
			System.err.println("Database error while searching for user data: " + ex.getMessage());
			System.out.println(ex);
			result = 2;
			return result;
		} finally {
			entityManager.close();
		}
		return result;
	}
	
	public static List<String> showLicensePlates() {
		List<String> result = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<String> query = entityManager.createQuery(selectAllLicensePlates, String.class);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during cars selection: " + ex.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
		return result;
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
	
	public static List<Reservation> selectReservations() {
		List<Reservation> result = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectAllReservations, Reservation.class);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during reservations selection: " + ex.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
		return result;
	}
	
	public static List<Reservation> selectReservations(String licensePlate) {
		List<Reservation> result = null;
		try {
			Car selectedCar = (Car) read(Car.class, licensePlate);
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectCarReservations, Reservation.class);
			query.setParameter("car", selectedCar);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during reservations selection: " + ex.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
		return result;
	}
	
	public static List<Reservation> selectReservations(User user) {
		List<Reservation> result = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectCustomerReservations, Reservation.class);
			query.setParameter("user", user);
			result = query.getResultList();
		}catch (Exception ex) {
			System.err.println("Exception during reservations selection: " + ex.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
		return result;
	}
	
	/*
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
	}*/
	
	public static List<Car> findAvailableCars(Date arrival, Date departure, String loc, int seats){
		List<Car> result = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Car> query = entityManager.createQuery(findAvailableCars, Car.class);
			query.setParameter("location", loc);
			query.setParameter("seatNumber", seats);
			query.setParameter("pickUpDate", arrival);
			query.setParameter("deliveryDate", departure);
			query.setParameter("pickUpDate", arrival);
			query.setParameter("deliveryDate", departure);
			query.setParameter("pickUpDate", arrival);
			query.setParameter("deliveryDate", departure);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception while searching for available cars" + ex.getMessage());
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
			query.setParameter("actualDate", Utils.getCurrentSqlDate());
			result = query.getResultList();
		}
		catch (Exception ex){
			System.err.println("Exception during reservation selection: " + ex.getMessage());
			return 2;
		}
		finally {
			entityManager.close();
		}
		if(result.isEmpty())
			return 0;
		else
			return 1;
	}
	
	/*
	public static int create(Reservation r) { 
		int reservation = selectActiveReservation(r);
		if (reservation == 1) {
			System.err.println("It's not permitted to book more than one car at a time");
			return 1;
		}
		else if (reservation == 2){
			return 2;
		}
		int result = create((Object) r);
		return result;
	}
	*/
	
	// Eugenia
	public static List<Car> selectAllCars() {
		List<Car> result = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Car> query = entityManager.createQuery(selectAllCars, Car.class);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during cars selection: " + ex.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
		return result;
	}
	
	//Eugenia
	// Find all the reservations giving a specific Car
	public static List <Reservation> selectReservations(Car car) {
		List <Reservation> reservations = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectCarReservations, Reservation.class);
			query.setParameter("car", car);
			reservations = query.getResultList();
		}catch(Exception ex) {
			System.err.println("Exception during reservations selection: " + ex.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
		return reservations;
	}
	
	// This function returns 1 if there is a reservation for the car, 0 if there isn't and 2 if there is an error in the DB
	public static int existsCarActiveReservations(Car car) {
		int result = 2;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectCarActiveReservations, Reservation.class);
			query.setParameter("car", car);
			query.setParameter("actualDate", Utils.getCurrentSqlDate());
			query.getSingleResult();
			result = 1;
		} catch(NoResultException ex) {
			System.err.println("There is no reservation for the car");
			return 0;
		} catch (Exception ex) {
			System.err.println("Exception during car active reservations selection: " + ex.getMessage());
			return 2;
		} finally {
			entityManager.close();
		}
		return result;
	}
	
	// Eugenia
	// Delete can be fail because: a customer have the car (RentHandler has to check if is it true), error in the DB
	public static boolean delete(Car car) {
		boolean result = false;
		try {
			car.setRemoved(true);
			result = update(car);
		}catch(Exception ex) {
			System.err.println("Exception during car deletion: " + ex.getMessage());
			return false;
		}
		return result;
	}
		
	public static void finish() {
		factory.close();
	}
}
