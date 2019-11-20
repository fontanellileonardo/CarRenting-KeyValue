
import javafx.event.*;
import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;


public class VisualTableCar extends TableView<Car> {
    private ObservableList<Car> CarList;
    
     public VisualTableCar(Boolean customer){
       
        TableColumn columnVendor = new TableColumn("VENDOR");
        columnVendor.setCellValueFactory(new PropertyValueFactory<>("vendor"));
        TableColumn columnSeat = new TableColumn("SEAT NUMBER");
        columnSeat.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        TableColumn columnKM = new TableColumn("KM");
        columnKM.setCellValueFactory(new PropertyValueFactory<>("kilometers"));
        TableColumn columnPrice = new TableColumn("DAY PRICE");
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        if(customer) {
        	getColumns().addAll(columnVendor, columnSeat, columnKM, columnPrice);
        	setEditable(true);
        }
        	
        
        else {
        	 TableColumn columnLicensePlate = new TableColumn("LICENSE PLATE");
             columnLicensePlate.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
             TableColumn columnLocation = new TableColumn("LOCATION");
             columnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
             TableColumn columnRemoved = new TableColumn("REMOVED");
             columnRemoved.setCellValueFactory(new PropertyValueFactory<>("removed"));
             
             getColumns().addAll(columnLicensePlate, columnVendor, columnSeat, columnLocation, columnKM, columnPrice, columnRemoved);
        }
        
        CarList = FXCollections.observableArrayList();
        setItems(CarList);
       
    }
    
    public void setTableCarStyle() {
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setPrefWidth(400);
        setMaxHeight(480);
    }
    
    public void carListUpdate(List<Car> cars){
        CarList.clear();
        CarList.addAll(cars);
    }
    
    public void clear() {
        CarList.clear();
    }
    
    public ObservableList<Car> getCars() {return CarList;}
    
}

