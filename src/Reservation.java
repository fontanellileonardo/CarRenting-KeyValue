import java.sql.Date;
import java.time.LocalDate;
import javafx.beans.property.*;
import javax.persistence.*;

@Entity
@Table(name = "Reservation")
public class Reservation {
	private long id;
	private SimpleObjectProperty<Date> pickUpDate;
	private SimpleObjectProperty<Date> deliveryDate;
	private User user;
	private Car car;
	
	public Reservation() {
		pickUpDate = new SimpleObjectProperty<Date> ();
		deliveryDate = new SimpleObjectProperty<Date> ();
	}
	
	public Reservation(LocalDate pickUpDate, LocalDate deliveryDate, User user, Car car) {
		this.pickUpDate = new SimpleObjectProperty<Date> (Utils.localDateToSqlDate(pickUpDate));
		this.deliveryDate = new SimpleObjectProperty<Date> (Utils.localDateToSqlDate(deliveryDate));
		this.user = user;
		this.car = car;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	public long getId() {
		return id;
	}
	
	@Column(name = "PickUpDate")
	public Date getPickUpDate() {
		return pickUpDate.get();
	}
	
	@Column(name = "DeliveryDate")
	public Date getDeliveryDate() {
		return deliveryDate.get();
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {})
	public User getUser() {
		return user;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {})
	public Car getCar() {
		return car;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setPickUpDate(Date  pickUpDate) {
		this.pickUpDate.set(pickUpDate);
	}
	
	public void setDeliveryDate(Date  deliveryDate) {
		this.deliveryDate.set(deliveryDate);
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setCar(Car car) {
		this.car = car;
	}
	
	public SimpleStringProperty fiscalCodeProperty() {
		return user.fiscalCodeProperty();
	}
	
	public SimpleStringProperty licensePlateProperty() {
		return car.licensePlateProperty();
	}
	
	public SimpleStringProperty vendorProperty() {
		return car.vendorProperty();
	}
	
	public SimpleIntegerProperty seatNumberProperty() {
		return car.seatNumberProperty();
	}
	
	public SimpleDoubleProperty priceProperty() {
		return car.priceProperty();
	}
	
	public SimpleDoubleProperty kilometersProperty() {
		return car.kilometersProperty();
	}
}
