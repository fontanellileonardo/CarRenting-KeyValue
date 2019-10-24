import java.time.*;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.Callback;
import javafx.geometry.*;

public class SearchPanel{ 
    private final static int    BOX_SPACING = 80, 
                                LABEL_SPACING = 170, 
                                PERIOD_BAR_SPACING = 5; 
    private final HBox box, labels, periodBar;
    final Label periodTitle;
    final Label from;
    private final DatePicker pickUpDate; 
    final Label to;
    private final DatePicker deliveryDate; 
    final Label pickUpLocation;
    private final ComboBox placeField;
    final Label seatsTitle;
    private final ComboBox seatsNumber;
    final Button search;
    
    
    final Callback<DatePicker, DateCell> deliveryCellFactory = new Callback<DatePicker, DateCell>() {
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item.compareTo(pickUpDate.getValue().plusDays(1)) < 0) {
                        // Tomorrow is too soon.
                        setDisable(true);
                    }
                }
            };
	     }
	 };
	 
	 final Callback<DatePicker, DateCell> pickUpCellFactory = new Callback<DatePicker, DateCell>() {
	        public DateCell call(final DatePicker datePicker) {
	            return new DateCell() {
	                public void updateItem(LocalDate item, boolean empty) {
	                    super.updateItem(item, empty);

	                    if (item.compareTo(LocalDate.now().plusDays(1)) < 0) {
	                        // Tomorrow is too soon.
	                        setDisable(true);
	                    }
	                }
	            };
		     }
		 };
 
    public SearchPanel() {
        box = new HBox(BOX_SPACING);
        //labels = new HBox(LABEL_SPACING);
        labels = new HBox();
        periodBar = new HBox(PERIOD_BAR_SPACING);
        periodTitle = new Label("Renting Period");
        from = new Label("From:");
        pickUpDate = new DatePicker(LocalDate.now().plusDays(1));
        pickUpDate.setDayCellFactory(pickUpCellFactory);
        to = new Label("To:");
        deliveryDate = new DatePicker(LocalDate.now().plusDays(1));
        deliveryDate.setDayCellFactory(deliveryCellFactory);
        pickUpLocation = new Label("Pick-up Location");
        labels.setMargin(pickUpLocation, new Insets(0,0,0,170));
        ObservableList<String> loc = 
                FXCollections.observableArrayList (
                    "Firenze","Pisa");
        placeField = new ComboBox(loc);
        placeField.setValue("Firenze");
        seatsTitle = new Label("Number of seats");
        labels.setMargin(seatsTitle,new Insets(0,0,0,98));
        ObservableList<String> seats = 
                FXCollections.observableArrayList (
                    "2","4","5","6");
        seatsNumber = new ComboBox(seats);
        seatsNumber.setValue("4");
        search = new Button("SEARCH");
        
        
        labels.getChildren().addAll(periodTitle, pickUpLocation, seatsTitle);
        periodBar.getChildren().addAll(from, pickUpDate, to, deliveryDate);
        box.getChildren().addAll(periodBar, placeField, seatsNumber, search);
        
    }
    
    public void setSearchPanelStyle(String font, double fontSize) {
        labels.setLayoutX(185);
        labels.setLayoutY(85);
        box.setLayoutX(80);
        box.setLayoutY(110);
        periodTitle.setFont(Font.font(font, fontSize)); 
        periodTitle.setLayoutX(40);
        from.setFont(Font.font(font, fontSize)); 
        to.setFont(Font.font(font, fontSize)); 
        pickUpDate.setPrefWidth(105); 
        deliveryDate.setPrefWidth(105); 
        pickUpLocation.setFont(Font.font(font, fontSize));
        seatsTitle.setFont(Font.font(font, fontSize));
        placeField.setPrefWidth(105);
        //search.setFont(Font.font(font, fontSize + 5));
    }
    
    public HBox getBox() { return box; }
    public HBox getLabels() { return labels; }
    public DatePicker getPickUpDate() { return pickUpDate; }
    public DatePicker getDeliveryDate() { return deliveryDate; }
    public ComboBox getPlaceField() {return placeField;}
    public ComboBox getSeatsNumber() {return seatsNumber;}
    public Button getSearch() {return search;}
}    

