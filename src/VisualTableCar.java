
import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;


public class VisualTableCar extends TableView<Car> {
    private ObservableList<Car> CarList;
    
     public VisualTableCar(){
       
        TableColumn columnVendor = new TableColumn("VENDOR");
        columnVendor.setCellValueFactory(new PropertyValueFactory<>("vendor"));
        TableColumn columnSeat = new TableColumn("SEAT NUMBER");
        columnSeat.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        TableColumn columnKM = new TableColumn("KM");
        columnKM.setCellValueFactory(new PropertyValueFactory<>("kilometers"));
        TableColumn columnPrice = new TableColumn("DAY PRICE");
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        CarList = FXCollections.observableArrayList();
        setItems(CarList);
        getColumns().addAll(columnVendor, columnSeat, columnKM, columnPrice);
    }
    
    public void setTableCarStyle() {
        setFixedCellSize(40);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setPrefWidth(160);
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

