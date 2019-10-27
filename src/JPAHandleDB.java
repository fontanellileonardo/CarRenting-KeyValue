import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import org.hibernate.exception.ConstraintViolationException;

public class JPAHandleDB {
	private static EntityManagerFactory factory;
	private static EntityManager entityManager;
	private static String selectAllCustomers = "SELECT u FROM User u WHERE u.customer = true";
	private static String selectAllFeedbacks = "SELECT f FROM Feedback f WHERE f.mark <= :minMark";
	private static String selectActiveReservation = "SELECT r FROM Reservation r WHERE r.user = :user AND r.pickUpDate > :actualDate";
	private static String findAvailableCars = "SELECT c FROM Car c WHERE c.location = :location AND c.seatNumber = :seatNumber AND c.idCar NOT IN "
												+ "(SELECT r.car FROM Reservation r WHERE (r.pickUpDate BETWEEN :pickUpDate AND :deliveryDate) "
												+ "OR (r.deliveryDate BETWEEN :pickUpDate AND :deliveryDate) "
												+ "OR (pickUpDate < :pickUpDate AND deliveryDate > :deliveryDate))";
	private static String selectReservations = "SELECT r FROM Reservation r WHERE r.car = :car";
	private static String selectAllCars = "SELECT c FROM Car c";
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
	
	public static List<Car> findAvailableCars(LocalDate arrival, LocalDate departure, String loc, int seats){
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
		LocalDate date = LocalDate.now();
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Reservation> query = entityManager.createQuery(selectActiveReservation, Reservation.class);
			query.setParameter("user", r.getUser()); 
			query.setParameter("actualDate", date);
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
	
	public static int insertNewReservation(Reservation r) { //Ho supposto che si passasse un oggetto Reservation a questo metodo. Se si vuole far passare gli stessi parametri della scorsa versione, si può anche cambiare (o fare un overloading)
		int reservation = selectActiveReservation(r);
		if (reservation == 1) {
			System.err.println("It's not permitted to book more than one car at a time");
			return 1;
		}
		else if (reservation == 2){
			return 2;
		}
		int result = create(r);
		return result;
	}
	
	// Eugenia
	public static List<Car> selectAllCars() {
		List<Car> result = null;
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Car> query = entityManager.createQuery(selectAllCars, Car.class);
			result = query.getResultList();
		} catch (Exception ex) {
			System.err.println("Exception during feedbacks selection: " + ex.getMessage());
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
			TypedQuery<Reservation> query = entityManager.createQuery(selectReservations, Reservation.class);
			query.setParameter("car", car);
			reservations = query.getResultList();
		}catch(Exception ex) {
			System.err.println("Exception during feedbacks selection: " + ex.getMessage());
			return null;
		} finally {
			entityManager.close();
		}
		return reservations;
	}
	
	// Eugenia
	// Eliminazione può fallire perchè: la macchina è prenotata (gestita dal RentHandler), errore nel db
	public static boolean delete(Car car) {
		boolean res = true;
		try {
			// Select all the reservations related to this car
			List <Reservation> reservations = selectReservations(car);	
			if(reservations != null) {
				System.out.println("Reservations found: " + reservations.size());
				// Modify the car object in reservation, because the previous car doen't exists anymore
				
				// Codice da eliminare
				List <Car> cars = selectAllCars();
				// Fine 
				for(int i = 0; i < reservations.size() && res != false; i++) {
					System.out.println("Reservation "+reservations.get(i).getId()+" id car before: "+reservations.get(i).getCar().getIdCar());
					reservations.get(i).setCar(null);
					res = update(reservations.get(i));
					System.out.println("Reservation "+reservations.get(i).getId()+" id car after: "+reservations.get(i).getCar().getIdCar());
				}
				// la delete va fatta dopo che ho scollegato tutte le reservations da questa car altrimenti JPA si incazza
				if(res == true)
					res = delete(Car.class,car.getIdCar());
			}	
		}catch(Exception ex) {
			System.err.println("Exception during feedbacks selection: " + ex.getMessage());
			return false;
		}
		return res;
	}
		
	
	public static int insertNewFeedback(Feedback f) { //discorso simile come sopra: ho supposto si passasse un oggetto Feedback, ma se si vuole far passare i parametri uno per uno e far costruire l'oggetto classe da questo metodo ci vuole 1 minuto a cambiare
		int result = create(f);
		return result;
	}
	
	public static void finish() {
		factory.close();
	}
	
}
