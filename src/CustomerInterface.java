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
    private final Label feedbackTitle;
    private final Label addFeedbackTitle;
    private final Label commentTitle;
    private final Label markTitle;
    // Message for the user. Green -> ok, Red -> an error occurs
    private final Text userMsg;
//	------ TEXT FIELDS ------ 
    private final TextArea commentField;
//	------ COMBO BOXES ------
    private final ComboBox markField;
//	------ TABLES ------
    private final VisualTableCar tableCar;
    private final VisualTableFeedback tableFeedback;
//	------ BUTTONS ------
    private final Button reserve;
    private final Button delete;
    private final Button logOut;
    private final Button addCommentButton;
//	------ BOXES ------
    private final SearchPanel searchPanel;
    private final HBox buttonBox;
    private final VBox sxPanel; 
    private final VBox dxPanel;
    private final GridPane insertFeedbackBox;
    private final GridPane tablesBox;
    private final VBox box;

    public CustomerInterface() { 
//		------ LABELS ------
    	title = new Label("Car Renting");
        tableTitle = new Label("AVAILABLE CARS");
        feedbackTitle = new Label("FEEDBACK");
        userMsg = new Text("");
        userMsg.setFont(Font.font("Calibri", 16));
        addFeedbackTitle = new Label("ADD NEW FEEDBACK");
        commentTitle = new Label("Comment");
        markTitle = new Label("Mark");
//    	------ TEXT FIELDS ------ 
        commentField = new TextArea();
        commentField.setWrapText(true);
//    	------ COMBO BOXES ------
        ObservableList<String> scores = 
                FXCollections.observableArrayList (
                    "1","2","3","4","5");
        markField = new ComboBox(scores);
        markField.setValue("5");
//		------ TABLES ------
        tableCar = new VisualTableCar(true);
        tableFeedback = new VisualTableFeedback();
// 		------ BUTTONS ------
        reserve = new Button("RESERVE");
        delete = new Button("DELETE RESERVATION");
        logOut = new Button("LOG OUT"); 
        addCommentButton = new Button("ADD");
//		------ BOXES ------
        searchPanel = new SearchPanel();
        buttonBox = new HBox(40);
        buttonBox.getChildren().addAll(reserve,delete);
        
        insertFeedbackBox = new GridPane();
        insertFeedbackBox.add(commentTitle,0,0);	insertFeedbackBox.add(markTitle,1,0);
        insertFeedbackBox.add(commentField,0,1);	insertFeedbackBox.add(markField,1,1);
        insertFeedbackBox.add(addCommentButton,2,1);
        
        sxPanel = new VBox(3); 
        sxPanel.getChildren().addAll(tableTitle, tableCar, buttonBox);
        dxPanel = new VBox(DX_PANEL_SPACE);
        dxPanel.getChildren().addAll(feedbackTitle,tableFeedback,addFeedbackTitle,insertFeedbackBox);
        tablesBox = new GridPane();
        tablesBox.add(sxPanel, 0, 0);	tablesBox.add(dxPanel, 1, 0);	
        box = new VBox();
        box.getChildren().addAll(logOut,title,
	                                //searchPanel.getLabels(),
	                                searchPanel.getBox(),userMsg,
	                                tablesBox  );
    }
    
// set the style
    public void setUserInterfaceStyle() { 
//		------ LABELS ------
        title.setFont(Font.font("Calibri", 11 + TITLE_SIZE)); 
        title.setLayoutX(340);
        title.setLayoutY(10);
        tableTitle.setFont(Font.font("Calibri", 5 + SECTION_SIZE));
        feedbackTitle.setFont(Font.font("Calibri", 5 + SECTION_SIZE));
//		------ TEXT FIELDS ------      
        commentField.setPrefSize(265,45);
        
//		------ TABLES ------
        tableCar.setTableCarStyle();
        tableFeedback.setTableFeedbackStyle();
// 		------ BUTTONS ------
        logOut.setLayoutX(750);
        logOut.setLayoutY(3);
//		------ BOXES ------
        searchPanel.setSearchPanelStyle("Calibri", 13); 
        sxPanel.setAlignment(Pos.TOP_CENTER);
        sxPanel.setSpacing(8);
        dxPanel.setAlignment(Pos.TOP_CENTER);
        dxPanel.setSpacing(8);
        //insertFeedbackBox.setHgap(1);
        insertFeedbackBox.setHgap(5);
        insertFeedbackBox.setAlignment(Pos.CENTER);
        //tablesBox.setVgap(5);
        tablesBox.setAlignment(Pos.TOP_CENTER);
        //tablesBox.setGridLinesVisible(true);
        tablesBox.setHgap(13);
        box.setStyle("-fx-background-color: aliceblue");
        box.setAlignment(Pos.CENTER);
        box.setMargin(logOut,new Insets(0,0,0,740));
        box.setSpacing(10);
        box.setPrefWidth(890);
        box.setPrefHeight(750);
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
        
        // add new comment
        addCommentButton.setOnAction((ActionEvent e)-> {
        	// take the values from the fields
        	if(rh.addFeedback(user, commentField.getText(),markField.getValue().toString())) {
        		userMsg.setFill(Color.GREEN);
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
        
        userMsg.setText("");
    }
    
    public VBox getBox() {return box;}
    public VisualTableCar getTableCar() { return tableCar;}
    public SearchPanel getSearchPanel() {return searchPanel;}
    
}