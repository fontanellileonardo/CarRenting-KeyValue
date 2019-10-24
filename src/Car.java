import javafx.beans.property.*;
import java.util.*;
import javax.persistence.*;

@Entity 
@Table(name="Car")
public class Car {
	
    private final SimpleIntegerProperty idCar; 
    private final SimpleStringProperty vendor;
    private final SimpleIntegerProperty seatNumber;
    private final SimpleStringProperty location;
    private final SimpleIntegerProperty kilometers;
    private final SimpleDoubleProperty price;
    private final SimpleStringProperty licencePlate;
    
    private List<Reservation> reservation =	new ArrayList<>();
    
    public Car() {
    	idCar = new SimpleIntegerProperty(0);
        vendor = new SimpleStringProperty("");
        seatNumber = new SimpleIntegerProperty(0);
        location = new SimpleStringProperty("");
        kilometers = new SimpleIntegerProperty(0);
        price = new SimpleDoubleProperty(0.0);
        licencePlate = new SimpleStringProperty("");
    }
    
    public Car(int id, String v, int s, String l, int k, Double pr, String p) {
        idCar = new SimpleIntegerProperty(id);
        vendor = new SimpleStringProperty(v);
        seatNumber = new SimpleIntegerProperty(s);
        location = new SimpleStringProperty(l);
        kilometers = new SimpleIntegerProperty(k);
        price = new SimpleDoubleProperty(pr);
        licencePlate = new SimpleStringProperty(p);
    }
    
    public void setVendor(String v) {vendor.set(v);}
    public void setSeatNumber(int s) {seatNumber.set(s);}
    public void setLocation(String l) {location.set(l);}
    public void setKilometers(int k) {kilometers.set(k);}
    public void setPrice(Double pr) {price.set(pr);}
    public void setLicencePlate(String p) {licencePlate.set(p);}
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IdCar", updatable=false, nullable=false )
    public int getIdCar() { return idCar.get();}
    @Column(name="Vendor")
    public String getVendor() { return vendor.get();}
    @Column(name="SeatNumber")
    public int getSeatNumber() { return seatNumber.get();}
    @Column(name="Location")
    public String getLocation() { return location.get();}
    @Column(name="Kilometers")
    public int getKilometers() {return kilometers.get();}
    @Column(name="Price")
    public Double getPrice() {return price.get();}
    @Column(name="LicencePlate", unique = true)
    public String getLicencePlate() {return licencePlate.get();}
    @OneToMany(
    		mappedBy = "car",
    		fetch = FetchType.LAZY,
    		cascade = {}
    		)
    public List<Reservation> getReservation() { return reservation; }
    
}

