import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;

public class HandleDB {
	
	private static Connection databaseConnection;
	private static PreparedStatement registerSt;
	private static PreparedStatement selectActiveReservationSt;
	private static PreparedStatement insertReservationSt;
	private static PreparedStatement selectScoreSt;
	private static PreparedStatement updateScoreSt;
	private static PreparedStatement insertScoreSt;
	private static PreparedStatement deleteReservationSt;
	private static PreparedStatement insertCarSt;
	private static PreparedStatement logInSt;
	private static PreparedStatement loadingAvailableCarsSt;
	private static PreparedStatement showRankSt;
	
	
	static {
		String connStr = "jdbc:mysql://localhost:3306/CarRenting?user=root&password=leonardo91&serverTimezone=UTC#";
		String sql;
		
		try {
			databaseConnection = DriverManager.getConnection(connStr);
			
			sql = "INSERT INTO User SET Email=?, Name=?, Surname=?, Password=?, Customer=? ";
			registerSt = databaseConnection.prepareStatement(sql);
			
			sql = "SELECT * FROM Reservation WHERE Email=? AND PickUpDate>?";
			selectActiveReservationSt = databaseConnection.prepareStatement(sql);
			
			sql = "INSERT INTO Reservation SET idCar=?, Email=?,  PickUpDate=?, DeliveryDate=?";
			insertReservationSt = databaseConnection.prepareStatement(sql);
			
			sql = "SELECT Score FROM CarRenting.Rank WHERE Email=?";
			selectScoreSt = databaseConnection.prepareStatement(sql);
			
			sql = "UPDATE CarRenting.Rank SET Score=? WHERE Email=?";
			updateScoreSt = databaseConnection.prepareStatement(sql);
			
			sql = "INSERT INTO CarRenting.Rank SET Email=?, Score=?";
			insertScoreSt = databaseConnection.prepareStatement(sql);
			
			sql = "DELETE FROM Reservation WHERE Email=? AND PickUpDate > ?";
			deleteReservationSt = databaseConnection.prepareCall(sql);
			
			sql = "INSERT INTO Car SET IDCar=?, Vendor=?, SeatNumber=?,  Location=?, Kilometers=?, Price=?";
			insertCarSt = databaseConnection.prepareStatement(sql);
			
			sql = "SELECT * FROM User WHERE Email=? AND Password=? AND Customer=?";
			logInSt = databaseConnection.prepareStatement(sql);
			
			sql = "SELECT * FROM Car WHERE Location = ? AND seatNumber = ? AND IDCar NOT IN "
			        + "(SELECT IDCar FROM reservation WHERE (PickUpDate BETWEEN ? AND ?)"
			        + "OR (DeliveryDate BETWEEN ? AND ?)"
			        + "OR (PickUpDate < ? AND DeliveryDate > ?))"; 
			loadingAvailableCarsSt = databaseConnection.prepareStatement(sql);
			
			sql = "SELECT Name, Surname, User.Email, Score FROM User JOIN CarRenting.Rank ON User.Email = CarRenting.Rank.Email " 
					+ "ORDER BY Score DESC LIMIT 10";
			showRankSt = databaseConnection.prepareStatement(sql);
			
		} catch(SQLException ex) {
			System.err.println("Errore Connessione SQL " + ex.getMessage());
		}
	}
	
	public static int registerUser(User user) {
		int insertedRows = 0;
		
		try {
			registerSt.setString(1, user.getEmail());
			registerSt.setString(2, user.getName());
			registerSt.setString(3, user.getSurname());
			registerSt.setString(4, user.getPassword());
			registerSt.setBoolean(5, user.getCustomer());
			
			insertedRows = registerSt.executeUpdate();
		} catch(SQLException ex) {
			if(ex.getErrorCode() == 1062) { //The user is already registered
				return 1;
			}
			System.err.println("Database error while registering a user: " + ex.getMessage());
			return 2;
		}
		if(insertedRows == 0)
			return 2;
		else
			return 0;
	}
	
	public static int selectActiveReservation(String email) {
		Date date = java.sql.Date.valueOf(LocalDate.now());

		try {
			selectActiveReservationSt.setString(1, email);
			selectActiveReservationSt.setDate(2, date);
			
			selectActiveReservationSt.execute();
			
			ResultSet rs = selectActiveReservationSt.getResultSet();
			
			if(rs.next() == true) //there is already an active reservation
				return 1;
			
		} catch(SQLException ex) {
			System.err.println("Database Error while searching for active reservations: " + ex.getMessage());
			return 2;
		}
		
		return 0;
		
	}
	
	public static int insertReservation(User user, Car car, LocalDate pickUpDate, LocalDate deliveryDate) { 
		int reservation = selectActiveReservation(user.getEmail());
		if(reservation == 1) {
			System.err.println("It's not permitted to book more than one car at a time");
			return 1;
		}

		Date pDate = java.sql.Date.valueOf(pickUpDate);
		Date dDate = java.sql.Date.valueOf(deliveryDate);
		
		try {
			insertReservationSt.setString(1, car.getIdCar());
			insertReservationSt.setString(2, user.getEmail());
			insertReservationSt.setDate(3, pDate);
			insertReservationSt.setDate(4, dDate);
			
			insertReservationSt.executeUpdate();
		} catch(SQLException ex) {
			System.err.println("Database error while inserting a reservation: " + ex.getMessage());
			return 2;
		}
		try {
			selectScoreSt.setString(1, user.getEmail());
			selectScoreSt.execute();
			
			ResultSet rs = selectScoreSt.getResultSet();
			
			if(rs.next() != false) { 
				int score = rs.getInt("Score") + 1;
				
				updateScoreSt.setInt(1, score);
				updateScoreSt.setString(2, user.getEmail());
				
				updateScoreSt.executeUpdate();
			}
			else { 
				int score = 1;
				
				insertScoreSt.setString(1, user.getEmail());
				insertScoreSt.setInt(2, score);
				
				insertScoreSt.executeUpdate();
			} 
		}catch(SQLException ex) {
			System.err.println("Database error while updating the rank after an insertion: " + ex.getMessage());
			return 2;
		}
		return 0;
	}
	
	public static boolean updateScore(String email, int m) {
		try {
			int modifiedRows = 0;
			selectScoreSt.setString(1, email);
			selectScoreSt.execute();
			
			ResultSet rs = selectScoreSt.getResultSet();
			rs.next();
			
			int score = rs.getInt("Score") + m;
			updateScoreSt.setInt(1, score);
			updateScoreSt.setString(2, email);
			
			modifiedRows = updateScoreSt.executeUpdate();
			if(modifiedRows == 0) {
				System.err.println("The user is not present in the rank table ");
				return false;
			}
		} catch (SQLException ex) {
			System.err.println("Database error while updating score in the rank after deleting a reservation: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	public static boolean deleteReservation(User user) { 
		
		Date date = java.sql.Date.valueOf(LocalDate.now());
		
		try{
			int deletedRows = 0;
			deleteReservationSt.setString(1, user.getEmail());
			deleteReservationSt.setDate(2, date);
			
			deletedRows = deleteReservationSt.executeUpdate();
			if(deletedRows == 0) {
				System.err.println("There is no reservation for that user ");
				return true;
			}
				
		} catch (SQLException ex) {
			System.err.println("Database error while deleting a reservation " + ex.getMessage());
			return false;
		}
		
		if(updateScore(user.getEmail(), -1) == false)
			return false;

		return true;
	}

	public static int insertCar(Car car) {
		int insertedRows = 0;
		
		try {
			insertCarSt.setString(1, car.getIdCar());
			insertCarSt.setString(2, car.getVendor());
			insertCarSt.setInt(3, car.getSeatNumber());
			insertCarSt.setString(4, car.getLocation());
			insertCarSt.setInt(5, car.getKilometers());
			insertCarSt.setDouble(6, car.getPrice());
			
			insertedRows = insertCarSt.executeUpdate();
		} catch(SQLException ex) {
			if(ex.getErrorCode() == 1062) //there is already a car with the same ID
				return 1;
			System.err.println("Database error while inserting a new car: " + ex.getMessage());
			return 2;
		}
		if(insertedRows == 0)
			return 2;
		else
			return 0;
	}
	
	public static int logIn(User user) {
		int result = 1;
		
		try {
			logInSt.setString(1, user.getEmail());
			logInSt.setString(2, user.getPassword());
                        logInSt.setBoolean(3,user.getCustomer());
			logInSt.execute();
			
			ResultSet rs = logInSt.getResultSet();
			if(rs.next() != false)
				result = 0;
		} catch(SQLException ex) {
			System.err.println("Database error while searching for user data " + ex.getMessage());
                        result = 2;
			return result;
		}
		return result;
	}
	
	public static List<Car> loadingAvailableCars(LocalDate arrive, LocalDate departure, String loc, String seats) {
        List<Car> carList = new ArrayList<>();
        try { 
        		Date arr = java.sql.Date.valueOf(arrive);
        		Date dep = java.sql.Date.valueOf(departure);
                int seatNumber = Integer.parseInt(seats);
                loadingAvailableCarsSt.setString(1,loc);
                loadingAvailableCarsSt.setInt(2,seatNumber);
                loadingAvailableCarsSt.setDate(3,arr);
                loadingAvailableCarsSt.setDate(4,dep);
                loadingAvailableCarsSt.setDate(5,arr);
                loadingAvailableCarsSt.setDate(6,dep);
                loadingAvailableCarsSt.setDate(7,arr);
                loadingAvailableCarsSt.setDate(8,dep);
                loadingAvailableCarsSt.execute();
                ResultSet rs = loadingAvailableCarsSt.getResultSet();
                while (rs.next())
                    carList.add(new Car(rs.getString("IDCar"), rs.getString("Vendor"), rs.getInt("SeatNumber"), rs.getString("Location"), rs.getInt("Kilometers"), rs.getDouble("Price")));
        } catch (SQLException ex) {
        	System.err.println("Database error while searching for available cars: " + ex.getMessage());
        	return null;
        }
        return carList;
    }

	public static List<User> showRank(){
		List<User> userList = new ArrayList<>();
		
		try {
			showRankSt.execute();
			ResultSet rs = showRankSt.getResultSet();
			
			while(rs.next()) {
				userList.add(new User(rs.getString("Name"), rs.getString("Surname"), false, rs.getString("Email"), null, rs.getInt("score")));
			}
			
		} catch(SQLException ex) {
			System.err.println("Error while retrieving the rank: " +ex.getMessage());
			return null;
		}
		return userList;
	}
	
	public static void finish() {
		try {
			databaseConnection.close();
		} catch(SQLException ex) {
			System.err.println("Database error while disconnecting " + ex.getMessage());
		}
	}
}
