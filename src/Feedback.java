import java.time.*;
import javax.persistence.*;

@Entity

public class Feedback {
	private long id;
	private int mark;
	private String comment;
	private LocalDate date;
	private User user;
	
	public Feedback() {}
	
	public Feedback(int mark, String comment, LocalDate date ,User user) {
		this.mark = mark;
		this.comment = comment;
		this.date = date;
		this.user = user;
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
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	
	@Column(name = "Comment")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Column(name = "Date")
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	@ManyToOne(
				fetch=FetchType.EAGER,
				cascade = {}
				)
	//@Column(name = "User")
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
