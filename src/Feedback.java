import java.sql.Date;
import javafx.beans.property.*;

public class Feedback extends FeedbackKV{
	private SimpleObjectProperty<User> user;
	
	public Feedback(Integer mark, String comment, Date date ,User user) {
		super(mark,comment,date);
		this.user = new SimpleObjectProperty<> (user); 
	}
	
	public Feedback() {
		super();
		user = new SimpleObjectProperty<User> ();
	}
	
	public User getUser() {
		return user.get();
	}
	
	public void setUser(User user) {
		this.user.set(user);
	}	
	
	public SimpleStringProperty nickNameProperty() {
		return user.get().nickNameProperty();
	}
	
	public SimpleStringProperty fiscalCodeProperty() {
		return user.get().fiscalCodeProperty();
	}
	
}
