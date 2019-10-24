import javafx.beans.property.*;
import java.util.List;
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
    
    @OneToMany(
    		mappedBy = "car",
    		fetch = FetchType.LAZY,
    		cascade = {}
    		)
    private List<Reservation> reservation =	new ArrayList<>();
    
    public Car() {}
    
    public Car(int id, String v, int s, String l, int k, Double pr, String p) {
        idCar = new SimpleIntegerProperty(id);
        vendor = new SimpleStringProperty(v);
        seatNumber = new SimpleIntegerProperty(s);
        location = new SimpleStringProperty(l);
        kilometers = new SimpleIntegerProperty(k);
        price = new SimpleDoubleProperty(pr);
        licencePlate = new SimpleStringProperty(p);
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
    @Column(name="LicencePlate")
    public String getLicencePlate() {return licencePlate.get();}
    
    
}

