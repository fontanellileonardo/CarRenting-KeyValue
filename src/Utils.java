import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Utils {
	
	public final static int	CAR_MANAGER = 1,
			USER_TABLE = 2,
			FEEDBACK_TABLE = 3,
			RESERVATION_TABLE = 4;
	
	public final static long MIN_MARK = 1L;
	public final static Integer	MAX_MARK = 5;
	
	// Return the current date (java.sql.Date)
	public static java.sql.Date getCurrentSqlDate() {
		Calendar cal = Calendar.getInstance();
		java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
		return date;
	}
	
	public static java.sql.Date localDateToSqlDate(LocalDate localDate) { 
		java.util.Date date = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());	
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}
	
	public static List<Feedback> convertFeedbackKV(List<FeedbackKV> feedKV, List<User> user){
		List<Feedback> result = new ArrayList<Feedback>();
		for(int i = 0; i < feedKV.size(); i++) {
			result.add(new Feedback(feedKV.get(i), user.get(i)));
		}
		return result;
	}
}
