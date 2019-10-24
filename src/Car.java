import javafx.beans.property.*;


public class Car {
    
    private final SimpleStringProperty idCar;
    private final SimpleStringProperty vendor;
    private final SimpleIntegerProperty seatNumber;
    private final SimpleStringProperty location;
    private final SimpleIntegerProperty kilometers;
    private final SimpleDoubleProperty price;
    
    public Car(String id, String v, int s, String l, int k, Double pr) {
        idCar = new SimpleStringProperty(id);
        vendor = new SimpleStringProperty(v);
        seatNumber = new SimpleIntegerProperty(s);
        location = new SimpleStringProperty(l);
        kilometers = new SimpleIntegerProperty(k);
        price = new SimpleDoubleProperty(pr);
    }
    
    public String getIdCar() { return idCar.get();}
    public String getVendor() { return vendor.get();}
    public int getSeatNumber() { return seatNumber.get();}
    public String getLocation() { return location.get();}
    public int getKilometers() {return kilometers.get();}
    public Double getPrice() {return price.get();}
    
}

