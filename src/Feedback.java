import java.sql.Date;
import javafx.beans.property.*;

public class Feedback{
	private final SimpleIntegerProperty mark;
	private final SimpleStringProperty comment;
	private final SimpleObjectProperty<Date> date;
	private final SimpleStringProperty nickName;
	private final SimpleStringProperty fiscalCode;
	
	public Feedback() {
		mark = new SimpleIntegerProperty(5);
		comment = new SimpleStringProperty("");
		date = new SimpleObjectProperty<Date> ();
		nickName = new SimpleStringProperty("");
		fiscalCode = new SimpleStringProperty("");
	}
	
	public Feedback(Integer mark, String comment, Date date, String nickName, String fiscalCode) {
		this.mark = new SimpleIntegerProperty(mark);
		this.comment = new SimpleStringProperty(comment);
		this.date = new SimpleObjectProperty<Date> (date);
		this.nickName = new SimpleStringProperty(nickName);
		this.fiscalCode = new SimpleStringProperty(fiscalCode);
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
	public String getNickName() {
		return  nickName.get();
	}
	public void setNickName(String nickName) {
		this.nickName.set(nickName);
	}
	public String getFiscalCode() {
		return  fiscalCode.get();
	}
	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode.set(fiscalCode);
	}
}
