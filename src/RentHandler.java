
import java.time.*;
import java.util.*;


public class RentHandler {
	
	// Open the connections with the dabases
	public RentHandler() {
		RiakHandleDB.openConnection();
		JPAHandleDB.openConnection();
	}
	
    // Register method
    public String register(User regUser) {
    	int ret;
        String outcome = "";
        if(regUser!=null) {
        	if(regUser.getFiscalCode().equals("") == false && regUser.getNickName().equals("") == false && regUser.getName().equals("") == false && regUser.getSurname().equals("") == false && 
        			regUser.getEmail().equals("") == false && regUser.getPassword().equals("") == false) {
        		//1) user already exists, 2) database error, 0) success
                ret = JPAHandleDB.create(regUser);
                switch (ret){
                    case 0:
                        System.out.println("Successfull registration!");
                        outcome = "You've been registered!";
                        return outcome;
                    case 1:
                        System.out.println("This user already exists! ");
                        outcome = "OOps! You are already registered!";
                        return outcome;
                    default:
                        System.out.println("Database Error");
                        outcome = "OOps! Something went wrong!:(";
                        return outcome;
                }
        	}
        }
        System.out.println("The registration fields are not correctly inserted");
        outcome = "OOps! You didn't complete "+'\n'+"    the registration fields";
        return outcome;
        
        } 
            
    // Login method
    public String login(User loggedUser){
        String outcome = "";
        int ret;
        if(loggedUser!=null) {
        	if(loggedUser.getEmail().equals("") == false && loggedUser.getPassword() != null) {
        		//1) user doen't exists 2) database error, 0) success
        		ret = JPAHandleDB.logIn(loggedUser);
                switch (ret){
                    case 0:
                        System.out.println("Successfull login!");
                        outcome = "Success!";
                        return outcome;
                    case 1:
                        System.out.println("The user attempt to insert wrong credentials! ");
                        outcome = "OOps! Wrong credentials";
                        return outcome;
                    default:
                        System.out.println("Database Error");
                        outcome = "OOps! Something went wrong!:(";
                        return outcome;
                }
        	}
        }
        System.out.println("The login fields are not correctly inserted");
        outcome = "OOps! You didn't insert "+'\n'+"        the login fields";
        return outcome;
    }
    
    // Insert a new car
    public String insertCar(Car car) {
        String outcome = "";
        int ret;
        if(car!=null) {
        	if(car.getLicensePlate().equals("") == false && car.getVendor().equals("") == false) {
        		// 1) the car already exists -> check if it is removed, if it is it is insert again
        		// 2) database error, 0) success
        		ret = JPAHandleDB.create(car);
                switch (ret){
                        case 0:
                            System.out.println("Successfull insertion!");
                            outcome = "Success!";
                            return outcome;
                        case 1:
                            System.out.println("The employer attempt to insert an existing car! ");
                            Car control = (Car) JPAHandleDB.read(Car.class, car.getLicensePlate());
                            if(control.getRemoved()) {
                            	control.setRemoved(false);
                            	control.setLocation(car.getLocation());
                            	control.setKilometers(car.getKilometers());
                            	control.setPrice(car.getPrice());
                            	boolean result = JPAHandleDB.update(control);
                            	if(result)
                            		outcome = "Updated!";
                            	else
                            		outcome = "Database Error";
                            }
                            else
                            	outcome = "OOps! This car already exists!";
                            return outcome;
                        default:
                            System.out.println("Database Error");
                            outcome = "OOps! Something went wrong!:(";
                            return outcome;
                } 
        	}
        } 
        System.out.println("The login fields are not correctly inserted");
        outcome = "OOps! You didn't insert "+'\n'+"        the login fields";
        return outcome;
    }
    
    // The function deletes a car if it has no active reservation, otherwise it returns an error.
    public String deleteCar(Car car) {
    	String outcome = "";
    	int ret = JPAHandleDB.existsCarActiveReservations(car);
    	switch (ret){
	        case 0: // There is no reservation for this car
	        	if (JPAHandleDB.delete(car)) {
	        		System.out.println("Successfull deletion!");
		            outcome = "Successful deletion!";
		            return outcome;
	        	} else {
	        		System.out.println("Database Error");
		            outcome = "OOps! Something went wrong!:(";
		            return outcome;
	        	}
	        case 1: // There is an active reservation for this car
	            System.out.println("The employer attempt to delete a car that has an active reservation! ");
	            outcome = "OOps! This car has an active reservation!";
	            return outcome;
	        default: // Database error
	            System.out.println("Database Error");
	            outcome = "OOps! Something went wrong!:(";
	            return outcome;
    	}
    }
    
    // Get all the feedbacks
    public List<Feedback> showFeedbacks() {
        return showFeedbacks(Utils.MAX_MARK);
    }
    
    // Get all the feedbacks according to their mark (if mark = 3 -> all feedback with mark = 1,2, and 3)
    public List<Feedback> showFeedbacks(Integer mark) {
        List<Feedback> result = new ArrayList<Feedback>();
        result = RiakHandleDB.selectAllFeedbacks(mark);
        if(result == null) {
        	System.out.println("Feedbacks not updated");
        	return null;
        }
        System.out.println("Feedbacks updated");
        return result;
    }
    
    // Add a new Feedback
    public boolean addFeedback(User user, String comment, String mark) {
    	Integer intMark = Integer.parseInt(mark);
    	Feedback feedback = new Feedback(intMark, comment, Utils.getCurrentSqlDate(), user.getNickName(), user.getFiscalCode());
    	int result = RiakHandleDB.create(feedback, user.getFiscalCode());
    	if (result == 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    // Get all the customers
    public List<User> showCustomers(){
    	List<User> customers = JPAHandleDB.selectAllCustomers();
    	return customers;
    }
    
    // This function allows to retrieve all the cars license plates 
    // to show them in the reservation filter (filtered by license plate)
    public List<String> retrieveAllLicensePlates() {
    	List<String> licensePlatesList = JPAHandleDB.showLicensePlates();
    	return licensePlatesList;
    }
    
    // Show only the reservations related to a selected car 
    public List<Reservation> showReservations(String licensePlate) {
    	List<Reservation> reservations = null;
    	if(licensePlate.compareTo("ALL") == 0) {
    		reservations = JPAHandleDB.selectReservations();
    	}
    	else
    		reservations = JPAHandleDB.selectReservations(licensePlate);
    	return reservations;
    }
    
    // Retrieve all the reservations of one user
    public List<Reservation> showReservations(User user) {
    	List<Reservation> reservations = null;
    	reservations = JPAHandleDB.selectReservations(user);
    	return reservations;
    }
    
    // Delete the reservation selected 
    public boolean delete(Reservation reservation) {
        boolean succ = JPAHandleDB.delete(Reservation.class,reservation.getId());
        if(succ) {
            System.out.println("Reservation deleted");
        }
        else {
            System.out.println("Database error");
        }
        return succ;
    }
    
    // Takes all the machines that are available and that have not been deleted from the system
    public List<Car> showAvailableCar(LocalDate pickUpDate, LocalDate deliveryDate, String locality, String seats, StringBuilder out) {
    	int numSeats = Integer.parseInt(seats);
        List<Car> carList = JPAHandleDB.findAvailableCars(Utils.localDateToSqlDate(pickUpDate), Utils.localDateToSqlDate(deliveryDate), 
        		locality, numSeats);
        if(carList == null){
           System.out.println("Database Error!"); 
           out.append("Try Again Later. Server not Found");
           carList = new ArrayList<>();
        }
        else{
           System.out.println("List retrived"); 
           out.append(""); 
        }
        return carList;
    }
    
    // Get all the cars
    public List<Car> showAllCars(){
    	List<Car> carList = JPAHandleDB.selectAllCars();
    	return carList;
    }
      
    // Add a new Reservation for the customer
    public String addReservation(User user, Car selectedCar, LocalDate pickUpDate, LocalDate deliveryDate) {
    	Reservation res = new Reservation(pickUpDate, deliveryDate, user, selectedCar);
    	System.out.println("DEBUG: Reservation fields: PickUp:"+res.getPickUpDate()+" delDate:"+res.getDeliveryDate());
        String outcome = "";
        //1) if user already has an active reservation, 2) database error, 0) inserting done
        int ret = JPAHandleDB.create(res);
            switch (ret){
                case 0:
                    System.out.println("Successfull Reservation!");
                    outcome = "Success!";
                    return outcome;
                case 1:
                    System.out.println("The reservation already exists");
                    outcome = "OOps! Something went wrong!:(";
                    return outcome;
                default:
                    System.err.println("Database Error");
                    outcome = "OOps! Something went wrong!:(";
                    return outcome;
            }                
    }
    
    // Close the connection with the DB
    public void closeConnections() {
        JPAHandleDB.finish();
        RiakHandleDB.finish(); 
    }
}
