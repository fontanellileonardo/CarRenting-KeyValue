import java.time.LocalDate;
import java.util.*;
import javafx.beans.property.*;
import javax.persistence.*;

@Entity
@Table(name = "Reservation")
public class Reservation {
	private long id;
	private LocalDate pickUpDate;
	private LocalDate deliveryDate;
	private User user;
	private Car car;
	
	public Reservation() {}
	
	public Reservation(LocalDate pickUpDate, LocalDate deliveryDate, User user, Car car) {
		this.pickUpDate = pickUpDate;
		this.deliveryDate = deliveryDate;
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
	public LocalDate getPickUpDate() {
		return pickUpDate;
	}
	
	@Column(name = "DeliveryDate")
	public LocalDate getDeliveryDate() {
		return deliveryDate;
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
	
	public void setPickUpDate(LocalDate pickUpDate) {
		this.pickUpDate = pickUpDate;
	}
	
	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
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
	
	public SimpleStringProperty licencePlateProperty() {
		return car.licencePlateProperty();
	}
}
