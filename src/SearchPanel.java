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
    
//	------ LABELS ------
    private final Label periodTitle;
    private final Label from;
    private final Label to;
    private final Label pickUpLocation;
    private final Label seatsTitle;
//	------ COMBO BOXES ------
    private final ComboBox placeField;
    private final ComboBox seatsNumber;
//	------ DATE PICKERS ------
    private final DatePicker pickUpDate; 
    private final DatePicker deliveryDate; 
//	------ BUTTONS ------
    private final Button search;
//	------ BOXES ------
    private final GridPane box;
    //private final HBox box, labels, periodBar;


    
    
    final Callback<DatePicker, DateCell> deliveryCellFactory = new Callback<DatePicker, DateCell>() {
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item.compareTo(pickUpDate.getValue()) < 0) {
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
//    	------ LABELS ------
        periodTitle = new Label("Renting Period");
        from = new Label("From:");
        to = new Label("To:");
        pickUpLocation = new Label("Pick-up Location");
        seatsTitle = new Label("Number of seats");
//    	------ DATE PICKERS ------
        pickUpDate = new DatePicker(LocalDate.now().plusDays(1));
        pickUpDate.setDayCellFactory(pickUpCellFactory);
        deliveryDate = new DatePicker(LocalDate.now().plusDays(1));
        deliveryDate.setDayCellFactory(deliveryCellFactory);
//    	------ COMBO BOXES ------
        ObservableList<String> loc = 
                FXCollections.observableArrayList (
                    "Firenze","Pisa");
        placeField = new ComboBox(loc);
        placeField.setValue("Firenze");
        ObservableList<String> seats = 
                FXCollections.observableArrayList (
                    "2","4","5","6");
        seatsNumber = new ComboBox(seats);
        seatsNumber.setValue("4");
//    	------ BUTTONS ------
        search = new Button("SEARCH");
//    	------ BOX ------
        box = new GridPane();
        //labels = new HBox(LABEL_SPACING);
        //labels = new HBox();
        //periodBar = new HBox(PERIOD_BAR_SPACING);   
        //labels.getChildren().addAll(periodTitle, pickUpLocation, seatsTitle);
        //periodBar.getChildren().addAll(from, pickUpDate, to, deliveryDate);
        //box.getChildren().addAll(periodBar, placeField, seatsNumber, search);
        box.add(periodTitle,0,0,4,1);	box.add(pickUpLocation,4,0);
        box.add(seatsTitle, 5, 0);
        box.add(from, 0, 1);		box.add(pickUpDate, 1, 1);
        box.add(to, 2, 1); 			box.add(deliveryDate, 3, 1);
        box.add(placeField, 4, 1); 	box.add(seatsNumber, 5, 1);
        box.add(search, 6, 1);
        //box.setGridLinesVisible(true);
        
    }
    
    public void setSearchPanelStyle(String font, double fontSize) {
        //labels.setLayoutX(185);
        //labels.setLayoutY(85);
        //box.setLayoutX(2000);
        //box.setLayoutY(110);
        periodTitle.setFont(Font.font(font, fontSize)); 
        //periodTitle.setLayoutX(40);
        from.setFont(Font.font(font, fontSize)); 
        to.setFont(Font.font(font, fontSize)); 
        pickUpDate.setPrefWidth(110); 
        deliveryDate.setPrefWidth(110); 
        pickUpLocation.setFont(Font.font(font, fontSize));
        seatsTitle.setFont(Font.font(font, fontSize));
        placeField.setPrefWidth(105);
        //labels.setMargin(seatsTitle,new Insets(0,0,0,95));
        box.setAlignment(Pos.CENTER);
        box.setVgap(8);
        box.setHgap(5);
        box.setHalignment(periodTitle, HPos.CENTER);
        //box.setHalignment(seatsNumber, HPos.CENTER);
        box.setMargin(seatsNumber,new Insets(0,60,0,0));
        box.setMargin(placeField,new Insets(0,30,0,0));
        //box.setHalignment(seatsNumber, HPos.LEFT);
        box.setMargin(deliveryDate,new Insets(0,30,0,0));
        //labels.setAlignment(Pos.CENTER);
        //search.setFont(Font.font(font, fontSize + 5));
    }
    
    public GridPane getBox() { return box; }
    //public HBox getLabels() { return labels; }
    public DatePicker getPickUpDate() { return pickUpDate; }
    public DatePicker getDeliveryDate() { return deliveryDate; }
    public ComboBox getPlaceField() {return placeField;}
    public ComboBox getSeatsNumber() {return seatsNumber;}
    public Button getSearch() {return search;}
}    

