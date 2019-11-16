import java.time.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;


public class CustomerInterface {
    
    private String seats;
    private String locality;
    private LocalDate pickUpDate;
    private LocalDate deliveryDate;
    private Car selectedCar;
    private int currentTable;
    
    
    private final static int    TITLE_SIZE = 30,
                                SECTION_SIZE = 9, 
                                DX_PANEL_SPACE = 3;
//	------ LABELS ------
    private final Label title;
    private final Label reservationsTitle;
    private final Label carsTitle;
    private final Label feedbackTitle;
    private final Label addFeedbackTitle;
    private final Label commentTitle;
    private final Label markTitle;
    // Message for the user. Green -> ok, Red -> an error occurs
    private final Text userMsg;
//	------ TEXT FIELDS ------ 
    private final Text welcomeMsg;
    private final TextArea commentField;
//	------ COMBO BOXES ------
    private final ComboBox markField;
//	------ TABLES ------
    private final VisualTableReservation tableReservation;
    private final VisualTableCar tableCar;
    private final VisualTableFeedback tableFeedback;
//	------ BUTTONS ------
    private final Button reservationListButton;
    private final Button reserve;
    private final Button delete;
    private final Button logOut;
    private final Button addCommentButton;
//	------ BOXES ------
    private final SearchPanel searchPanel;
    private final HBox topButtonBox;
    private final HBox buttonBox;
    private final VBox sxPanel; 
    private final VBox dxPanel;
    private final GridPane insertFeedbackBox;
    private final GridPane tablesBox;
    private final VBox box;

    public CustomerInterface() { 
//		------ LABELS ------
    	title = new Label("Car Renting");
        reservationsTitle = new Label("YOUR RESERVATIONS");
        carsTitle = new Label("AVAILABLE CARS");
        feedbackTitle = new Label("FEEDBACK");
        userMsg = new Text("");
        userMsg.setFont(Font.font("Calibri", 16));
        addFeedbackTitle = new Label("ADD NEW FEEDBACK");
        commentTitle = new Label("Comment");
        markTitle = new Label("Mark");
//    	------ TEXT FIELDS ------ 
        welcomeMsg = new Text();
        commentField = new TextArea();
        commentField.setWrapText(true);
//    	------ COMBO BOXES ------
        ObservableList<String> scores = 
                FXCollections.observableArrayList (
                    "1","2","3","4","5");
        markField = new ComboBox(scores);
        markField.setValue("5");
//		------ TABLES ------
        tableReservation = new VisualTableReservation(true);
        tableCar = new VisualTableCar(true);
        tableFeedback = new VisualTableFeedback(false);
// 		------ BUTTONS ------
        reservationListButton = new Button("SHOW RESERVATIONS");
        reserve = new Button("RESERVE");
        delete = new Button("DELETE RESERVATION");
        logOut = new Button("LOG OUT"); 
        addCommentButton = new Button("ADD");
//		------ BOXES ------
        searchPanel = new SearchPanel();
        topButtonBox = new HBox(20);
        topButtonBox.getChildren().addAll(welcomeMsg,reservationListButton,logOut);
        buttonBox = new HBox(40);
        buttonBox.getChildren().addAll(reserve,delete);
        
        insertFeedbackBox = new GridPane();
        insertFeedbackBox.add(commentTitle,0,0);	insertFeedbackBox.add(markTitle,1,0);
        insertFeedbackBox.add(commentField,0,1);	insertFeedbackBox.add(markField,1,1);
        insertFeedbackBox.add(addCommentButton,2,1);
        
        sxPanel = new VBox(3); 
        sxPanel.getChildren().addAll(reservationsTitle, tableReservation, buttonBox);
        dxPanel = new VBox(DX_PANEL_SPACE);
        dxPanel.getChildren().addAll(feedbackTitle,tableFeedback,addFeedbackTitle,insertFeedbackBox);
        tablesBox = new GridPane();
        tablesBox.add(sxPanel, 0, 0);	tablesBox.add(dxPanel, 1, 0);	
        box = new VBox();
        box.getChildren().addAll(topButtonBox,title,
	                                searchPanel.getBox(),userMsg,
	                                tablesBox  );
    }
    
// set the style
    public void setUserInterfaceStyle() { 
//		------ LABELS ------
        title.setFont(Font.font("Calibri", 11 + TITLE_SIZE)); 
        title.setLayoutX(340);
        title.setLayoutY(10);
        reservationsTitle.setFont(Font.font("Calibri", 5 + SECTION_SIZE));
        carsTitle.setFont(Font.font("Calibri", 5 + SECTION_SIZE));
        feedbackTitle.setFont(Font.font("Calibri", 5 + SECTION_SIZE));
//		------ TEXT FIELDS ------      
        commentField.setPrefSize(265,45);
        
//		------ TABLES ------
        tableReservation.setTableReservationStyle();
        tableCar.setTableCarStyle();
        tableFeedback.setTableFeedbackStyle();
// 		------ BUTTONS ------
        logOut.setLayoutX(750);
        logOut.setLayoutY(3);
//		------ BOXES ------
        topButtonBox.setAlignment(Pos.CENTER_RIGHT);
        searchPanel.setSearchPanelStyle("Calibri", 13); 
        sxPanel.setAlignment(Pos.TOP_CENTER);
        sxPanel.setSpacing(8);
        dxPanel.setAlignment(Pos.TOP_CENTER);
        dxPanel.setSpacing(8);
        insertFeedbackBox.setHgap(5);
        insertFeedbackBox.setAlignment(Pos.CENTER);
        tablesBox.setAlignment(Pos.TOP_CENTER);
        tablesBox.setHgap(13);
        box.setStyle("-fx-background-color: aliceblue");
        box.setAlignment(Pos.CENTER);
        box.setMargin(topButtonBox,new Insets(0,20,0,400));
        box.setSpacing(10);
        box.setPrefWidth(890);
        box.setPrefHeight(750);
    }
    
    // Disable (true) or enable (false) a button
    void buttonBoxHandler(boolean disable) {
        reserve.setDisable(disable);
        delete.setDisable(!disable);
    }
    
    // listen the events from the search panel
    private void searchEventHandler(RentHandler rh) {
    	
        searchPanel.getSearch().setOnAction((ActionEvent ev)-> {
        	// takes dates from the calendars
            pickUpDate = searchPanel.getPickUpDate().getValue();
            deliveryDate = searchPanel.getDeliveryDate().getValue();
            // check if the delivery date is > pick-up date
            if(deliveryDate.compareTo(pickUpDate) >= 0) {
            	// Insert the tableCar and remove the tableReservation
            	userMsg.setFill(Color.RED);
            	locality = searchPanel.getPlaceField().getValue().toString();
                seats = searchPanel.getSeatsNumber().getValue().toString();
                StringBuilder outcome = new StringBuilder("");
                // update the car table with the available cars 
                changeTable(Utils.CAR_MANAGER);
                tableCar.carListUpdate(rh.showAvailableCar(pickUpDate, deliveryDate, locality, seats, outcome));                	                
                userMsg.setText(outcome.toString());
            } else {
            	userMsg.setFill(Color.RED);
            	userMsg.setText("The delivery date has to be greater than the pick-up date");
            }
            
        });
    }
    
    // activate the show reservation button and manage the events sent by it
    private void showReservationsEventHandler(RentHandler rh, User user) {
    	reservationListButton.setOnAction((ActionEvent ev)-> {
        	if(changeTable(Utils.RESERVATION_TABLE))
        		tableReservation.ListReservationUpdate(rh.showReservations(user));
        });
    }
    
    // listen the events from the Customer interface
    public void appEventHandler(User user, RentHandler rh, CarRenting carR) {
    	
    	// "initialization" phase
    	welcomeMsg.setText("Welcome "+user.getNickName());
    	currentTable = Utils.RESERVATION_TABLE;
    	tableReservation.ListReservationUpdate(rh.showReservations(user));
    	tableFeedback.ListFeedbackUpdate(rh.showFeedbacks());
    	reserve.setDisable(true);
    	delete.setDisable(true);
        searchEventHandler(rh);
        showReservationsEventHandler(rh,user);
        
        // Customer log-out
        logOut.setOnAction((ActionEvent e)-> {
            clearAll();
            carR.setScene("logout");
        });
        
        // Listen when the customer select a car from table car
        tableReservation.getSelectionModel().selectedIndexProperty().addListener((num) ->
        {       
        	// select an empty field
            if(tableReservation.getSelectionModel().getSelectedItem() == null) {
               tableReservation.getSelectionModel().clearSelection();

            }
            else {
            	// take the field selected from the car table
            	Reservation selectedReservation;
                selectedReservation = tableReservation.getSelectionModel().getSelectedItem();
                System.out.println("Selected reservation Id: "+selectedReservation.getId());
                //buttonBoxHandler(false);
                delete.setDisable(false);
                
                // Delete a reservation
                delete.setOnAction((ActionEvent e)-> {	
                	String outcome = "";
                	// Check if the Reservation is active -> it can be deleted
                	if(selectedReservation.getPickUpDate().compareTo(Utils.getCurrentSqlDate())>0) {
                        if(rh.delete(selectedReservation)) {
                        	userMsg.setFill(Color.GREEN);
                        	tableReservation.ListReservationUpdate(rh.showReservations(user));
                        	outcome = "Your reservation has been deleted";	
                        	delete.setDisable(true);
                        }
                        else {
                        	userMsg.setFill(Color.RED);
                        	outcome = "Oops, something went wrong, try later! :(";
                        }	
                	}
                	else {
                		// The selected reservation is not active
                		userMsg.setFill(Color.RED);
                		outcome = "The Reservation can't be deleted";
                	}
                    userMsg.setText(outcome);
                });             
            }
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
                System.out.println("I selected: "+selectedCar.getLicensePlate());
                buttonBoxHandler(false);
                
                // Reserve a car
                reserve.setOnAction((ActionEvent e)-> {
                	System.out.println("DEBUG: pickUp:"+pickUpDate+" delDate:"+deliveryDate);
	                String outcome = rh.addReservation(user, selectedCar, pickUpDate, deliveryDate);
	                if(outcome.equals("Success!")) {
	                	userMsg.setFill(Color.GREEN);
	                	changeTable(Utils.RESERVATION_TABLE);
	                	tableReservation.ListReservationUpdate(rh.showReservations(user));
	                	userMsg.setText(outcome);
	                	reserve.setDisable(true);
	                	delete.setDisable(true);
	                }     
	                else
	                    userMsg.setText(outcome);  
                });               
            }
        });
        
        // add new comment
        addCommentButton.setOnAction((ActionEvent e)-> {
        	// take the values from the fields
        	if(rh.addFeedback(user, commentField.getText(),markField.getValue().toString())) {
        		userMsg.setFill(Color.GREEN);
        		userMsg.setText("Your comment has been successfully added");
        		tableFeedback.ListFeedbackUpdate(rh.showFeedbacks());
        	} else {
        		userMsg.setFill(Color.RED);
        		userMsg.setText("Ooops, something went wrong");
        	}
        	clearFeedbackForm();
        });
    }
    
    public void clearFeedbackForm() {
    	commentField.setText("");
    	markField.setValue("5");
    }
    
    // reset Customer page
    public void clearAll() {
        searchPanel.getPickUpDate().setValue(LocalDate.now().plusDays(1));
        searchPanel.getDeliveryDate().setValue(LocalDate.now().plusDays(1));
        searchPanel.getPlaceField().setValue("Firenze");
        searchPanel.getSeatsNumber().setValue("4");
        commentField.setText("");
        clearFeedbackForm();
        changeTable(Utils.RESERVATION_TABLE);
        
        userMsg.setText("");
    }
    
    // show carTable or ReservationTable. Return true if it change the table, false otherwise
    private boolean changeTable(int table) {

    	// check if the table is already visible in the CustomerInterface
    	if(currentTable != table) {
    		// change the table
    		boolean ret = true;
    		if (table == Utils.RESERVATION_TABLE) {
    			ret = sxPanel.getChildren().removeAll(carsTitle,tableCar);
    			if(ret) {	// no error in remove
    				currentTable = table;
            		sxPanel.getChildren().add(0, reservationsTitle);
                	sxPanel.getChildren().add(1, tableReservation);
                	reserve.setDisable(true);
    			} else {
    				userMsg.setFill(Color.RED);
                	userMsg.setText("Oops, something went wrong! :(");
                	return false;
    			}
        	} else {
        		// Utils.CAR_MANAGER
        		ret = sxPanel.getChildren().removeAll(reservationsTitle,tableReservation);
        		if(ret) {	// no error in remove
        			currentTable = table;
            		sxPanel.getChildren().add(0, carsTitle);
                	sxPanel.getChildren().add(1, tableCar);
                	//buttonBoxHandler(false);
        		} else {
        			userMsg.setFill(Color.RED);
                	userMsg.setText("Oops, something went wrong! :(");
                	return false;
        		}
        	}
    		return true;
    	} 
    	return false;
    }
    
    public VBox getBox() {return box;}
    public VisualTableCar getTableCar() { return tableCar;}
    public SearchPanel getSearchPanel() {return searchPanel;}
    
}