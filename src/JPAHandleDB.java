import java.sql.Date;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.*;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.service.spi.ServiceException;

public class JPAHandleDB {
	private static EntityManagerFactory factory;
	private static EntityManager entityManager;
	private static String findUser = "SELECT u FROM User u WHERE u.email= :email AND u.password= :password AND u.customer= :customer ";
	private static String selectAllCustomers = "SELECT u FROM User u WHERE u.customer = true";
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

	// Open the connection with the DB
	public static void openConnection() {
		factory = DBConnection.getInstance().factory;
	}
	
	/* Create new object. 
	 * Return 0 -> everything is ok
	 * Return 1 -> object already exists or a unique value already exists
	 * Return 2 -> DB error
	 */
	public static int create(Object o) {
		System.out.println("Add a new object");
		try {
			if(factory == null)
				return 2;
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
			if(factory != null)
				entityManager.close();
		}
		return 0;
	}
	
	// Fetch the object
	public static <T> Object read(Class<T> entity, Object id) {
		System.out.println("Getting a new object");
		Object o = null;
		try {
			if(factory == null)
				return null;
			entityManager = factory.createEntityManager();
			o = entityManager.find(entity, id);
		} catch (Exception ex) {
			System.err.println("Exception during read: " + ex.getMessage());
		} finally {
			if(factory != null)
				entityManager.close();
		}
		return o;
	}
	
	// Update the object
	public static boolean update(Object o) {
		System.out.println("Update an object");
		try {
			if(factory == null)
				return false;
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.merge(o);
			entityManager.getTransaction().commit();
		} catch(Exception ex) {
			System.err.println("Exception during update: " + ex.getMessage());
			return false;
		} finally {
			if(factory != null)
				entityManager.close();
		}
		return true;
	}
	
	// Delete the object
	public static <T> boolean delete(Class<T> entity, Object id) {
		System.out.println("Delete an object");
		try {
			if(factory == null)
				return false;
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			Object o = entityManager.getReference(entity, id);
			entityManager.remove(o);
			entityManager.getTransaction().commit();
		} catch(Exception ex) {
			System.err.println("Exception during delete: " + ex.getMessage());
			return false;
		} finally {
			if(factory != null)
				entityManager.close();
		}
		return true;
	}
	
	// Check if the user exists in the DB. 
	// Return: 0 -> success, 1 -> the user doens't exist, 2 -> DB Error
	public static int logIn(User user) {
		int result = 1;
		User retrievedUser = null;
		try {
			// Check if there is the connection with the DB
			if(factory == null)
				return 2;
			// Try to get the user
			entityManager = factory.createEntityManager();
			TypedQuery<User> query = entityManager.createQuery(findUser, User.class);
			query.setParameter("email", user.getEmail());
			query.setParameter("password", user.getPassword());
			query.setParameter("customer", user.getCustomer());
			retrievedUser = query.getSingleResult();
			// Set the user object
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
			if(factory != null)
				entityManager.close();
		}
		return result;
	}
	
	// Get all the LicensePlates
	public static List<String> showLicensePlates() {
		List<String> result = null;
		try {
			// Check if there is the connection with the DB
			if(factory == null)
				return null;
			entityManager = factory.createEntityManager();
			TypedQuery<String> query = entityManager.createQuery(selectAllLicensePlates, String.class);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during cars selection: " + ex.getMessage());
			return null;
		} finally {
			if(factory != null)
				entityManager.close();
		}
		return result;
	}
	
	// Get all the customers
	public static List<User> selectAllCustomers() {
		List<User> result = null;
		try {
			// Check if there is the connection with the DB
			if(factory == null)
				return null;
			entityManager = factory.createEntityManager();
			TypedQuery<User> query = entityManager.createQuery(selectAllCustomers, User.class);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during customers selection: " + ex.getMessage());
			return null;
		} finally {
			if(factory != null)
				entityManager.close();
		}
		return result;
	}
	
	// Get all the Reservations
	public static List<Reservation> selectReservations() {
		List<Reservation> result = null;
		try {
			// Check if there is the connection with the DB
			if(factory == null)
				return null;
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectAllReservations, Reservation.class);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during reservations selection: " + ex.getMessage());
			return null;
		} finally {
			if(factory != null)
				entityManager.close();
		}
		return result;
	}
	
	// Get all car's reservation
	public static List<Reservation> selectReservations(String licensePlate) {
		List<Reservation> result = null;
		try {
			// Check if there is the connection with the DB
			if(factory == null)
				return null;
			// Get the car obj
			Car selectedCar = (Car) read(Car.class, licensePlate);
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectCarReservations, Reservation.class);
			query.setParameter("car", selectedCar);
			// Get the car's reservation
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during reservations selection: " + ex.getMessage());
			return null;
		} finally {
			if(factory != null)
				entityManager.close();
		}
		return result;
	}
	
	// Get all customer's reservation
	public static List<Reservation> selectReservations(User user) {
		List<Reservation> result = null;
		try {
			if(factory == null)
				return null;
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectCustomerReservations, Reservation.class);
			query.setParameter("user", user);
			result = query.getResultList();
		}catch (Exception ex) {
			System.err.println("Exception during reservations selection: " + ex.getMessage());
			return null;
		} finally {
			if(factory != null)
				entityManager.close();
		}
		return result;
	}
	
	// Get all the available cars
	public static List<Car> findAvailableCars(Date arrival, Date departure, String loc, int seats){
		List<Car> result = null;
		try {
			if(factory == null)
				return null;
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
			if(factory != null)
				entityManager.close();
		}
		return result;
	}
	
	// Get all the active reservations (active = the car was taken but not yet returned )
	public static int selectActiveReservation(Reservation r) {
		List<Reservation> result = null;
		try {
			if(factory == null)
				return 2;
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
			if(factory != null)
				entityManager.close();
		}
		if(result.isEmpty())
			return 0;
		else
			return 1;
	}
	
	// Get all the cars
	public static List<Car> selectAllCars() {
		List<Car> result = null;
		try {
			if(factory == null)
				return null;
			entityManager = factory.createEntityManager();
			TypedQuery<Car> query = entityManager.createQuery(selectAllCars, Car.class);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during cars selection: " + ex.getMessage());
			return null;
		} finally {
			if(factory != null)
				entityManager.close();
		}
		return result;
	}
	
	// Get all the reservations giving a specific Car
	public static List <Reservation> selectReservations(Car car) {
		List <Reservation> reservations = null;
		try {
			if(factory == null)
				return null;
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectCarReservations, Reservation.class);
			query.setParameter("car", car);
			reservations = query.getResultList();
		}catch(Exception ex) {
			System.err.println("Exception during reservations selection: " + ex.getMessage());
			return null;
		} finally {
			if(factory != null)
				entityManager.close();
		}
		return reservations;
	}
	 
	// This function returns 1 if exists a reservation for a specific car, 
	// 0 if there isn't and 2 if there is an error in the DB
	public static int existsCarActiveReservations(Car car) {
		int result = 2;
		try {
			if(factory == null)
				return 2;
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
			if(factory != null)
				entityManager.close();
		}
		return result;
	}
	
	// Delete an existing car
	// Delete can be fail because: a customer have the car (RentHandler has to check if is it true) or is already deleted
	// or there is an error in the DB
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
		
	// Close the connection
	public static void finish() {
		if(factory != null)
			factory.close();
	}
}
