import java.time.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;


public class CustomerInterface {
    
    String seats;
    String locality;
    LocalDate pickUpDate;
    LocalDate deliveryDate;
    Car selectedCar;
    
    
    private final static int    TITLE_SIZE = 30,
                                SECTION_SIZE = 9, 
                                DX_PANEL_SPACE = 3; 
//	------ LABELS ------
    private final Label title;
    private final Label tableTitle;
    private final Label rankTitle;
    // Message for the user. Green -> ok, Red -> an error occurs
    private final Text userMsg;
//	------ TABLES ------
    private final VisualTableCar tableCar;
    private final VisualTableFeedback tableFeedback;
//	------ BUTTONS ------
    private final Button reserve;
    private final Button delete;
    private final Button logOut;
//	------ BOXES ------
    private final SearchPanel searchPanel;
    private final HBox buttonBox;
    private final VBox sxPanel; 
    private final VBox dxPanel;
    private final AnchorPane box;

    public CustomerInterface() { 
//		------ LABELS ------
    	title = new Label("Car Renting");
        tableTitle = new Label("AVAILABLE CARS");
        rankTitle = new Label("FEEDBACK");
        userMsg = new Text();
        userMsg.setFont(Font.font("Calibri", 16));
//		------ TABLES ------
        tableCar = new VisualTableCar();
        tableFeedback = new VisualTableFeedback();
// 		------ BUTTONS ------
        reserve = new Button("RESERVE");
        delete = new Button("DELETE RESERVATION");
        logOut = new Button("LOG OUT");  
//		------ BOXES ------
        searchPanel = new SearchPanel();
        buttonBox = new HBox(40);
        buttonBox.getChildren().addAll(reserve,delete);
        sxPanel = new VBox(3); 
        sxPanel.getChildren().addAll(userMsg,tableTitle, tableCar, buttonBox);
        sxPanel.setAlignment(Pos.CENTER);
        dxPanel = new VBox(DX_PANEL_SPACE);
        dxPanel.getChildren().addAll(rankTitle, tableFeedback);
        box = new AnchorPane();
        box.getChildren().addAll(   logOut,title,
                                            searchPanel.getLabels(),
                                            searchPanel.getBox(),
                                            sxPanel, dxPanel  );
    }
    
// set the style
    public void setUserInterfaceStyle() { 
//		------ LABELS ------
        title.setFont(Font.font("Calibri", 11 + TITLE_SIZE)); 
        title.setLayoutX(340);
        title.setLayoutY(10);
        tableTitle.setFont(Font.font("Calibri", 5 + SECTION_SIZE));
        rankTitle.setFont(Font.font("Calibri", 5 + SECTION_SIZE));
//		------ TABLES ------
        tableCar.setTableCarStyle();
        tableFeedback.setTableFeedbackStyle();
// 		------ BUTTONS ------
        logOut.setLayoutX(750);
        logOut.setLayoutY(3);
//		------ BOXES ------
        searchPanel.setSearchPanelStyle("Calibri", 11); 
        sxPanel.setLayoutX(60);
        sxPanel.setLayoutY(150);
        sxPanel.setPrefSize(400,500);
        dxPanel.setLayoutX(530);
        dxPanel.setLayoutY(150);
        dxPanel.setPrefSize(300, 450);
        dxPanel.fillWidthProperty();
        dxPanel.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: aliceblue");
        box.setPrefWidth(890);
        box.setPrefHeight(660);
        box.setTopAnchor(sxPanel,129.0);
    }
    
    // Disable (true) or enable (false) a button
    void buttonBoxHandler(boolean disable) {
        reserve.setDisable(disable);
        //delete.setDisable(!disable);
    }
    
    // listen the events from the search panel
    void searchEventHandler(RentHandler rh) {
    	
        searchPanel.getSearch().setOnAction((ActionEvent ev)-> {
        	// takes dates from the calendars
            pickUpDate = searchPanel.getPickUpDate().getValue();
            deliveryDate = searchPanel.getDeliveryDate().getValue();
            // check if the delivery date is > pick-up date
            if(deliveryDate.compareTo(pickUpDate) >= 0) {
            	userMsg.setFill(Color.GREEN);
            	locality = searchPanel.getPlaceField().getValue().toString();
                seats = searchPanel.getSeatsNumber().getValue().toString();
                StringBuilder outcome = new StringBuilder("");
                // update the car table with the available cars 
                tableCar.carListUpdate(rh.showAvailableCar(pickUpDate, deliveryDate, locality, seats, outcome));
                userMsg.setText(outcome.toString());
            } else {
            	userMsg.setFill(Color.RED);
            	userMsg.setText("The delivery date has to be greater than the pick-up date");
            }
            
        });
    }
    
    // listen the events from the Customer interface
    public void appEventHandler(User user, RentHandler rh, CarRenting carR) {

    	// "initialization" phase
    	tableFeedback.ListFeedbackUpdate(rh.showFeedbacks());
        buttonBoxHandler(true);
        searchEventHandler(rh);
        
        // Customer log-out
        logOut.setOnAction((ActionEvent e)-> {
            clearAll();
            carR.setScene("logout");
        });
        
        // Delete a reservation
        delete.setOnAction((ActionEvent e)-> {
            String outcome = rh.deleteReservation(user);
            userMsg.setText(outcome);
        });
        
        // Listen when the customer select a car from table car
        tableCar.getSelectionModel().selectedIndexProperty().addListener((num) ->
        {       
        	// select an empty field
            if(tableCar.getSelectionModel().getSelectedItem() == null) {
               tableCar.getSelectionModel().clearSelection();

            }
            else {
            	// take the field selected from the car table
                selectedCar = tableCar.getSelectionModel().getSelectedItem();
                System.out.println("I selected: "+selectedCar.getLicencePlate());
                buttonBoxHandler(false);
                
                // Reserve a car
                reserve.setOnAction((ActionEvent e)-> {
	                String outcome = rh.addReservation(user, selectedCar, pickUpDate, deliveryDate);
	                if(outcome.equals("Success!")) {
	                	// remove from table the car selected from the customer
	                	tableCar.getCars().remove(tableCar.getSelectionModel().getSelectedIndex());
	                	buttonBoxHandler(true);
	                	userMsg.setText(outcome);
	                }     
	                else
	                    userMsg.setText(outcome);  
                });               
            }
        });
    }
    
    // reset Customer page
    public void clearAll() {
        searchPanel.getPickUpDate().setValue(LocalDate.now().plusDays(1));
        searchPanel.getDeliveryDate().setValue(LocalDate.now().plusDays(1));
        searchPanel.getPlaceField().setValue("Firenze");
        searchPanel.getSeatsNumber().setValue("4");
        tableCar.clear();
        userMsg.setText("");
    }
    
    public AnchorPane getBox() {return box;}
    public VisualTableCar getTableCar() { return tableCar;}
    public SearchPanel getSearchPanel() {return searchPanel;}
    
}