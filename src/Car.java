import javafx.beans.property.*;
import java.util.*;
import javax.persistence.*;

@Entity 
@Table(name="Car")
public class Car {
	
	private final SimpleStringProperty licencePlate;
    private final SimpleStringProperty vendor;
    private final SimpleIntegerProperty seatNumber;
    private final SimpleStringProperty location;
    private final SimpleDoubleProperty kilometers;
    private final SimpleDoubleProperty price;
    private final SimpleBooleanProperty removed;
    
    private List<Reservation> reservations =	new ArrayList<>();
    
    public Car() {
    	licencePlate = new SimpleStringProperty("");
    	vendor = new SimpleStringProperty("");
        seatNumber = new SimpleIntegerProperty(0);
        location = new SimpleStringProperty("");
        kilometers = new SimpleDoubleProperty(0);
        price = new SimpleDoubleProperty(0.0);
        removed = new SimpleBooleanProperty(false);
    }
    
    public Car(String v, int s, String l, int k, Double pr, String p, Boolean r) {
    	licencePlate = new SimpleStringProperty(p);
    	vendor = new SimpleStringProperty(v);
        seatNumber = new SimpleIntegerProperty(s);
        location = new SimpleStringProperty(l);
        kilometers = new SimpleDoubleProperty(k);
        price = new SimpleDoubleProperty(pr);
        removed = new SimpleBooleanProperty(r);
    }
    
    public void setVendor(String v) {vendor.set(v);}
    public void setSeatNumber(int s) {seatNumber.set(s);}
    public void setLocation(String l) {location.set(l);}
    public void setKilometers(double k) {kilometers.set(k);}
    public void setPrice(Double pr) {price.set(pr);}
    public void setLicencePlate(String p) {licencePlate.set(p);}
    public void setRemoved(Boolean removed) {this.removed.set(removed);}
    public void setReservations(List<Reservation> res) {reservations = res;}
    
    @Id
    @Column(name="LicencePlate", unique = true)
    public String getLicencePlate() {return licencePlate.get();}
    @Column(name="Vendor")
    public String getVendor() { return vendor.get();}
    @Column(name="SeatNumber")
    public int getSeatNumber() { return seatNumber.get();}
    @Column(name="Location")
    public String getLocation() { return location.get();}
    @Column(name="Kilometers")
    public double getKilometers() {return kilometers.get();}
    @Column(name="Price")
    public Double getPrice() {return price.get();}
    @Column(name="Removed")
    public Boolean getRemoved() { return removed.get(); }
    @OneToMany(
    		mappedBy = "car",
    		fetch = FetchType.LAZY,
    		cascade = {}
    		)
    public List<Reservation> getReservations() { return reservations; }
    
}

