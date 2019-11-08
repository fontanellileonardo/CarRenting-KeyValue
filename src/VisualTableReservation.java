import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class VisualTableReservation extends TableView<Reservation>{
	 private ObservableList<Reservation> reservationsList;
	    
     public VisualTableReservation(boolean customer){
    	 TableColumn columnLicensePlate = new TableColumn("LICENSE PLATE");
		 columnLicensePlate.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
		 TableColumn columnPickUpDate = new TableColumn("PICKUP DATE");
		 columnPickUpDate.setCellValueFactory(new PropertyValueFactory<>("pickUpDate"));
		 TableColumn columnDeliveryDate = new TableColumn("DELIVERY DATE");
		 columnDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
    	 if(customer) {
    		 getColumns().addAll(columnLicensePlate, columnPickUpDate, columnDeliveryDate);
    	 } else {
    		 TableColumn columnFiscalCode = new TableColumn("FISCAL CODE");
 	         columnFiscalCode.setCellValueFactory(new PropertyValueFactory<>("fiscalCode"));
 	         getColumns().addAll(columnFiscalCode, columnLicensePlate, columnPickUpDate, columnDeliveryDate);
    	 }
        reservationsList = FXCollections.observableArrayList();
        setItems(reservationsList);
        
    }
    
    public void setTableReservationStyle() {
        //setFixedCellSize(90);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setPrefWidth(400);
        setMaxHeight(480);
    }
    
    public void ListReservationUpdate(List<Reservation> reservations){
    	reservationsList.clear();
    	reservationsList.addAll(reservations);
    }
    
    public ObservableList<Reservation> getReservations() {return reservationsList;}
    
}



