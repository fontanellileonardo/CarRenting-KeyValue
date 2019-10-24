import javafx.beans.property.*;

public class User {
    private final SimpleStringProperty name;
    private final SimpleStringProperty surname;
    private final SimpleBooleanProperty customer;
    private final SimpleStringProperty email;
    private final SimpleStringProperty password;
    private final SimpleIntegerProperty score;
    
    public User() {
        name = new SimpleStringProperty("");
        surname = new SimpleStringProperty("");
        customer = new SimpleBooleanProperty(true);
        email = new SimpleStringProperty("");
        password = new SimpleStringProperty("");
        score = new SimpleIntegerProperty(0); 
    }
    
    public User(String n, String c, Boolean cust, String e, String pwd, int sco)
    {
        name = new SimpleStringProperty(n);
        surname = new SimpleStringProperty(c);
        customer = new SimpleBooleanProperty(cust);
        email = new SimpleStringProperty(e);
        password = new SimpleStringProperty(pwd);
        score = new SimpleIntegerProperty(sco);
        
    }
    
    public void setName(String n) { name.set(n); }
    public void setSurname(String s) { surname.set(s); }
    public void setEmail(String e) { email.set(e); } 
    public void setCustomer(boolean c) { customer.set(c); }
    public void setPassword(String p) { password.set(p); }
    public void setScore(int sc) { score.set(sc); }
    
    public String getName() { return name.get(); }
    public String getSurname() { return surname.get(); }
    public String getEmail() { return email.get(); } 
    public Boolean getCustomer() {return customer.get(); }
    public String getPassword() { return password.get(); }
    public int getScore() { return score.get(); }
    
}
