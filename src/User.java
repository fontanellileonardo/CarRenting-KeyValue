import java.util.*;
import javax.persistence.*;
import javafx.beans.property.*;

@Entity
@Table(name="User")
public class User {
	private final SimpleStringProperty fiscalCode;
	private final SimpleStringProperty nickName;
    private final SimpleStringProperty name;
    private final SimpleStringProperty surname;
    private final SimpleBooleanProperty customer;
    private final SimpleStringProperty email;
    private final SimpleStringProperty password;
    private List<Feedback> feedbacks;
    private List<Reservation> reservations;
    
    public User() {
    	fiscalCode = new SimpleStringProperty("");
    	nickName = new SimpleStringProperty("");
    	feedbacks = new ArrayList<>();
    	reservations = new ArrayList<>();
        name = new SimpleStringProperty("");
        surname = new SimpleStringProperty("");
        customer = new SimpleBooleanProperty(true);
        email = new SimpleStringProperty("");
        password = new SimpleStringProperty("");
    }
    
    public User(String cf, String nm, String n, String c, Boolean cust, String e, String pwd)
    {
    	fiscalCode = new SimpleStringProperty(cf);
    	nickName = new SimpleStringProperty(nm);
    	feedbacks = new ArrayList<>();
    	reservations = new ArrayList<>();
        name = new SimpleStringProperty(n);
        surname = new SimpleStringProperty(c);
        customer = new SimpleBooleanProperty(cust);
        email = new SimpleStringProperty(e);
        password = new SimpleStringProperty(pwd);
        
    }
    
    public void setFiscalCode(String cf) {fiscalCode.set(cf);}
    public void setNickName(String nm) {nickName.set(nm);}    
    public void setName(String n) { name.set(n); }
    public void setSurname(String s) { surname.set(s); }
    public void setEmail(String e) { email.set(e); } 
    public void setCustomer(boolean c) { customer.set(c); }
    public void setPassword(String p) { password.set(p); }
    public void setFeedbacks(List<Feedback> feed) {feedbacks = feed;}
    public void setReservations(List<Reservation> res) {reservations = res;}
    
    @Id
    @Column(name = "FiscalCode", unique = true)
    public String getFiscalCode() { return fiscalCode.get();}

    @Column(name = "NickName", unique = true)
    public String getNickName() { return nickName.get();}  
    
    @Column(name = "Name")
    public String getName() { return name.get(); }
    
    @Column(name = "Surname")
    public String getSurname() { return surname.get(); }
    
    @Column(name = "Email", unique = true)
    public String getEmail() { return email.get(); } 
    
    @Column(name = "Customer")
    public Boolean getCustomer() {return customer.get(); }
    
    @Column(name = "Password")
    public String getPassword() { return password.get(); }
        
    @OneToMany(
    			mappedBy = "user",
    			fetch = FetchType.LAZY,
    			cascade = {}
    		)
    public List<Feedback> getFeedbacks() { return feedbacks;}
    @OneToMany(
			mappedBy = "user",
			fetch = FetchType.LAZY,
			cascade = {}
		)
    public List<Reservation> getReservations() { return reservations;}
    
   @Override 
    public String toString() {
    	return nickName.get();
    }
}
