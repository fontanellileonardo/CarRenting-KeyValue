import java.sql.Date;
import javax.persistence.*;
import javafx.beans.property.*;

@Entity

public class Feedback {
	private long id;
	private final SimpleIntegerProperty mark;
	private final SimpleStringProperty comment;
	private final SimpleObjectProperty<Date> date;
	private SimpleObjectProperty<User> user;
	
	public Feedback() {
		mark = new SimpleIntegerProperty(0);
		comment = new SimpleStringProperty("");
		date = new SimpleObjectProperty<Date> ();
		user = new SimpleObjectProperty<User> ();
	}
	
	public Feedback(int mark, String comment, Date date ,User user) {
		this.mark = new SimpleIntegerProperty(mark);
		this.comment = new SimpleStringProperty(comment);
		this.date = new SimpleObjectProperty<Date> (date);
		this.user = new SimpleObjectProperty<> (user); 
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idFeedback")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "Mark")
	public int getMark() {
		return mark.get();
	}
	public void setMark(int mark) {
		this.mark.set(mark);
	}
	
	@Column(name = "Comment")
	public String getComment() {
		return comment.get();
	}
	public void setComment(String comment) {
		this.comment.set(comment);
	}
	
	@Column(name = "Date")
	public Date getDate() {
		return date.get();
	}
	public void setDate(Date date) {
		this.date.set(date);
	}
	
	@ManyToOne(
				fetch=FetchType.EAGER,
				cascade = {}
				)
	//@Column(name = "User")
	public User getUser() {
		return user.get();
	}
	
	public void setUser(User user) {
		this.user.set(user);
	}
	
	public SimpleStringProperty nickNameProperty() {
		return user.get().nickNameProperty();
	}
}
