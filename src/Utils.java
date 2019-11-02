import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

public class Utils {
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
}
