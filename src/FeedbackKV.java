import java.sql.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class FeedbackKV {
	private final SimpleIntegerProperty mark;
	private final SimpleStringProperty comment;
	private final SimpleObjectProperty<Date> date;
	
	public FeedbackKV() {
		mark = new SimpleIntegerProperty(5);
		comment = new SimpleStringProperty("");
		date = new SimpleObjectProperty<Date> ();
	}
	
	public FeedbackKV(Integer mark, String comment, Date date) {
		this.mark = new SimpleIntegerProperty(mark);
		this.comment = new SimpleStringProperty(comment);
		this.date = new SimpleObjectProperty<Date> (date);
	}
	
	public Integer getMark() {
		return mark.get();
	}
	public void setMark(Integer mark) {
		this.mark.set(mark);
	}

	public String getComment() {
		return comment.get();
	}
	public void setComment(String comment) {
		this.comment.set(comment);
	}
	
	public Date getDate() {
		return date.get();
	}
	public void setDate(Date date) {
		this.date.set(date);
	}
}
