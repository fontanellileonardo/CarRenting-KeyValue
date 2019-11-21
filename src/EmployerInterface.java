import javafx.collections.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.util.*;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;


public class EmployerInterface {
    
    
    private final static int    TITLE_SIZE = 30,
                                SECTION_SIZE = 9, 
                                DX_PANEL_SPACE = 3; 
    
    private Car selectedCar = null;
    
//	------ LABELS ------
    private final Label title;
    private final Text errorMsgInsertion;
    private final Text errorMsgDeletion;
    private final Text errorMsgFeedback;
    private final Label insertTitle;
    final Label licensePlate,vendor,seatNumber,location,kilometers,price;
    private final Label feedbackTitle;
    private final Label userTitle;
    private final Label carTitle;
    private final Label reservationTitle;
    private final Label licensePlateFilter;
    private final Label selectMarkTitle;
//	------ TEXT FIELDS ------
    private final TextField fieldLicensePlate; 
    private final TextField fieldVendor; 
    private final TextField fieldKm; 
    private final TextField fieldPrice;
//	------ COMBO BOXES ------
    private final ComboBox fieldLocation;
    private final ComboBox fieldSeats;
    private final ComboBox<String> tableChoose;
    private final ComboBox<String> filterFeedback;
    private final ComboBox<String> filterReservation;
//	------ TABLES ------
    private final VisualTableFeedback tableFeedback;
    private final VisualTableUser tableUser;
    private final VisualTableCar tableCar;
    private final VisualTableReservation tableReservation;
//	------ BUTTONS ------
    final Button insertButton;
    final Button deleteButton;
    final Button logOutButton;
//	------ BOXES ------
    private final VBox insertPanel;
    private final VBox dxPanel;
    private final VBox userPanel;
    private final VBox carPanel;
    private final VBox feedbackPanel;
    private final VBox reservationPanel;
    private final AnchorPane box;
    
    private int table = Utils.CAR_MANAGER;
    private boolean firstOpen = true;
     
    public EmployerInterface() {
//    	------ LABELS ------
        title = new Label("Employer Management");
        errorMsgInsertion = new Text();
        errorMsgDeletion = new Text();
        errorMsgFeedback = new Text();
        insertTitle = new Label("Insert a new car");
        licensePlate = new Label("License Plate:");
        vendor = new Label("Vendor:");
        location = new Label("Location:");
        kilometers = new Label("Kilometers:");
        price = new Label("Price:");
        seatNumber = new Label("Seat Number:");
        feedbackTitle = new Label("FEEDBACKS");
        userTitle = new Label("USERS");
        carTitle = new Label("CARS");
        reservationTitle = new Label("RESERVATIONS");
        licensePlateFilter= new Label("License plate:");
        selectMarkTitle = new Label("Mark:");
//    	------ TEXT FIELDS ------
        fieldLicensePlate = new TextField();
        fieldVendor = new TextField();
        fieldKm = new TextField();
        fieldPrice = new TextField();
//		------ COMBO BOXES ------
        ObservableList<String> loc = 
                FXCollections.observableArrayList (
                    "Firenze","Pisa");
        fieldLocation = new ComboBox(loc);
        fieldLocation.setValue("Firenze");
        ObservableList<String> seats = 
                FXCollections.observableArrayList (
                    "2","4","5","6");
        fieldSeats = new ComboBox(seats);
        fieldSeats.setValue("4");
        ObservableList<String>  choose = 
        		FXCollections.observableArrayList (
        				"Car Manager", "User Table", "Feedback Table", "Reservation Table");
        tableChoose = new ComboBox(choose);
        tableChoose.setValue("Car Manager");
        ObservableList<String> filter =
        		FXCollections.observableArrayList (
        				"1", "2", "3", "4", "5");
        filterFeedback = new ComboBox(filter);
        filterFeedback.setValue("5");
        ObservableList<String> lpFilter =
        		FXCollections.observableArrayList("ALL");
        filterReservation = new ComboBox(lpFilter);
        filterReservation.setValue("ALL");
        
        		
//    	------ TABLES ------
        tableFeedback = new VisualTableFeedback(true);
        tableUser = new VisualTableUser(); 
        tableCar = new VisualTableCar(false);
        tableReservation = new VisualTableReservation(false);
//    	------ BUTTONS ------
        insertButton = new Button("INSERT");
        deleteButton = new Button("DELETE");
        logOutButton = new Button("LOG OUT");
//    	------ BOXES ------
        insertPanel = new VBox(3);
        insertPanel.getChildren().addAll(insertTitle, errorMsgInsertion,licensePlate,fieldLicensePlate,vendor,
                fieldVendor,seatNumber,fieldSeats, location,
                fieldLocation,kilometers, fieldKm, price, 
                fieldPrice, insertButton, deleteButton);
        feedbackPanel = new VBox(DX_PANEL_SPACE);
        feedbackPanel.getChildren().addAll(feedbackTitle, errorMsgFeedback, tableFeedback,selectMarkTitle,filterFeedback);
        userPanel = new VBox(DX_PANEL_SPACE); 
        userPanel.getChildren().addAll(userTitle, tableUser);
        carPanel = new VBox(DX_PANEL_SPACE);
        carPanel.getChildren().addAll(carTitle, errorMsgDeletion, tableCar, deleteButton);
        reservationPanel = new VBox(DX_PANEL_SPACE);
        reservationPanel.getChildren().addAll(reservationTitle, tableReservation,licensePlateFilter,filterReservation);
        dxPanel = new VBox(8);
        dxPanel.getChildren().addAll(tableChoose, carPanel);
        box = new AnchorPane();
        box.getChildren().addAll(title, logOutButton, insertPanel, dxPanel);
    }
    
    public void setEmpInterfaceStyle() { 
//    	------ LABELS ------
        title.setFont(Font.font("Calibri", 11 + TITLE_SIZE)); 
        title.setLayoutX(240);
        title.setLayoutY(10);
        insertTitle.setFont(Font.font("Calibri", 16));
        errorMsgInsertion.setFont(Font.font("Calibri", 16));
        errorMsgInsertion.setFill(Color.RED);
        errorMsgDeletion.setFont(Font.font("Calibri", 16));
        errorMsgDeletion.setFill(Color.RED);
        errorMsgFeedback.setFont(Font.font("Calibri", 16));
        errorMsgFeedback.setFill(Color.RED);
//    	------ TABLES ------
        tableFeedback.setTableFeedbackStyle();
        tableUser.setTableUserStyle(); //tabella User
        tableCar.setTableCarStyle();
        tableReservation.setTableReservationStyle(); 
//		------ BUTTONS ------
        logOutButton.setLayoutX(830);
        logOutButton.setLayoutY(3);
//    	------ BOXES ------
        insertPanel.setLayoutX(60);
        insertPanel.setLayoutY(100);
        insertPanel.setPrefSize(300,450);
        insertPanel.setAlignment(Pos.CENTER);
        
        dxPanel.setLayoutX(400);
        dxPanel.setLayoutY(100);
        dxPanel.setPrefSize(500,450);
        dxPanel.setAlignment(Pos.CENTER_LEFT);
        dxPanel.fillWidthProperty();
        
        feedbackPanel.setAlignment(Pos.CENTER);
        userPanel.setAlignment(Pos.CENTER);
        carPanel.setAlignment(Pos.CENTER);
        reservationPanel.setAlignment(Pos.CENTER);
 
        box.setStyle("-fx-background-color: khaki"); 
        box.setPadding(new Insets(0,5,0,0));
        box.setPrefWidth(950);
        box.setPrefHeight(600);
        box.setTopAnchor(insertPanel,129.0);
    }
    
    // listen the events 
    void empEventHandler(RentHandler rh, CarRenting carR){ 
    	// "initialization" phase
    	tableCar.carListUpdate(rh.showAllCars()); //Car table is the first one to be shown

    	
    	if(firstOpen) {
    		filterReservation.getItems().addAll(rh.retrieveAllLicensePlates());
    		firstOpen = false;
    	}
    	
    	filterReservation.setVisibleRowCount(filterReservation.getItems().size());
    	
    	// logout
        logOutButton.setOnAction((ActionEvent e)-> {
            errorMsgInsertion.setText("");
            errorMsgDeletion.setText("");
            if(table != Utils.CAR_MANAGER) {
                dxPanel.getChildren().remove(1); 
                dxPanel.getChildren().addAll(carPanel);
            }  
            clearAll();
          
    		table = Utils.CAR_MANAGER;
    		tableChoose.setValue("Car Manager");

            carR.setScene("logout");
        });
        
        // ComboBox event handler. Change the table on the dxPanel
        tableChoose.setOnAction((ActionEvent ev)-> { 
        	if(tableChoose.getValue() == "User Table") {
                if(table == Utils.CAR_MANAGER) {	
                	dxPanel.getChildren().removeAll(carPanel);
                }
                else if(table == Utils.FEEDBACK_TABLE) { 
                	dxPanel.getChildren().removeAll(feedbackPanel);
                }
                else if(table == Utils.RESERVATION_TABLE) {
                	dxPanel.getChildren().remove(reservationPanel);
                }
                table = Utils.USER_TABLE;
                dxPanel.getChildren().add(userPanel); 
            	tableUser.UserListUpdate(rh.showCustomers());
        	}
        	
        	else if(tableChoose.getValue() == "Car Manager") {
        		if(table == Utils.USER_TABLE) {
        			dxPanel.getChildren().removeAll(userPanel);
        		}
        		else if(table == Utils.FEEDBACK_TABLE) {
        			dxPanel.getChildren().removeAll(feedbackPanel);
        		}
        		else if(table == Utils.RESERVATION_TABLE) {
                	dxPanel.getChildren().remove(reservationPanel);
                }
        		table = Utils.CAR_MANAGER;
        		tableCar.carListUpdate(rh.showAllCars());
        		dxPanel.getChildren().addAll(carPanel);
        	}
        	
        	else if(tableChoose.getValue() == "Feedback Table") {
        		if(table == Utils.CAR_MANAGER) {
        			dxPanel.getChildren().removeAll(carPanel);
        		}
        		else if(table == Utils.USER_TABLE) {
        			dxPanel.getChildren().removeAll(userPanel);
        		}
        		else if(table == Utils.RESERVATION_TABLE) {
                	dxPanel.getChildren().remove(reservationPanel);
                }
        		table = Utils.FEEDBACK_TABLE;
        		filterFeedback.setValue("5");
        		dxPanel.getChildren().addAll(feedbackPanel);
        		List<Feedback> feedbacks = new ArrayList<>();
        		feedbacks = rh.showFeedbacks();
        		if(feedbacks == null) {
        			errorMsgFeedback.setText("Ooops! Feedbacks not available");
        		} else {
        			errorMsgFeedback.setText("");
        			tableFeedback.ListFeedbackUpdate(feedbacks);
        		}
        	}
        	
        	else if(tableChoose.getValue() == "Reservation Table") {
        		if(table == Utils.CAR_MANAGER) {
        			dxPanel.getChildren().removeAll(carPanel);
        		}
        		else if(table == Utils.USER_TABLE) {
        			dxPanel.getChildren().removeAll(userPanel);
        		}
        		else if(table == Utils.FEEDBACK_TABLE) {
                	dxPanel.getChildren().remove(feedbackPanel);
                }
        		table = Utils.RESERVATION_TABLE;
        		System.out.println("Resetto lista");
        		filterReservation.setValue("ALL");
        		dxPanel.getChildren().add(reservationPanel);
            	tableReservation.ListReservationUpdate(rh.showReservations("ALL"));
        	}
        	
        });
        
        
        // insert car
        insertButton.setOnAction((ActionEvent ev)-> {
        	// takes field from the form fields
            String loc = fieldLocation.getValue().toString();
            int seats = Integer.parseInt(fieldSeats.getValue().toString());
            // Check if all the fields are correct
            String outcome = "";
            if(fieldLicensePlate.getText().equals("") == false && fieldVendor.getText().equals("") == false &&
            		fieldKm.getText().matches("^\\d+\\.?[0-9]*$") == true && 
            		fieldPrice.getText().matches("^\\d+\\.?[0-9]*$") == true ) {
            	Car car = new Car(fieldVendor.getText(),seats,loc,Double.parseDouble(fieldKm.getText()),
            			Double.parseDouble(fieldPrice.getText()),fieldLicensePlate.getText(), false);
            	// try to insert new car in the DB
            	outcome = rh.insertCar(car);
                if(outcome.equals("Success!")) {
                	errorMsgInsertion.setFill(Color.GREEN);
                	//Insert the new car also in the list used for the reservations filter
                	filterReservation.getItems().add(car.getLicensePlate()); 
                    clearAll();
                } else 
                	errorMsgInsertion.setFill(Color.RED);
            } else {
            	errorMsgInsertion.setFill(Color.RED);
            	outcome = "You have to insert all the fields correctly";
            	System.out.println("Insert car form is not correct");
            }
            if(table == Utils.CAR_MANAGER)
                tableCar.carListUpdate(rh.showAllCars());
            errorMsgInsertion.setText(outcome);
        });
        
        deleteButton.setOnAction((ActionEvent ev)-> {
        	selectedCar = null;
        	selectedCar = tableCar.getSelectionModel().getSelectedItem();
        	String outcome = "";
        	if(selectedCar != null && !selectedCar.getRemoved()) {
        		
        		outcome = rh.deleteCar(selectedCar);
        		if(outcome.equals("Successful deletion!")) {
        				errorMsgDeletion.setFill(Color.GREEN);
        		}
        		else
        			errorMsgDeletion.setFill(Color.RED);
        		errorMsgDeletion.setText(outcome);
        		tableCar.carListUpdate(rh.showAllCars());
        	}
        	else if (selectedCar != null && selectedCar.getRemoved()) {
        		errorMsgDeletion.setFill(Color.RED);
        		errorMsgDeletion.setText("Car already deleted");
        	}
        });
        
        filterFeedback.setOnAction((ActionEvent ev)-> {
        	List<Feedback> feedbacks = new ArrayList<>();
    		feedbacks = rh.showFeedbacks(Integer.parseInt(filterFeedback.getValue().toString()));
    		if(feedbacks == null) {
    			errorMsgFeedback.setText("Ooops! Feedbacks not available");
    		} else {
    			errorMsgFeedback.setText("");
    			tableFeedback.ListFeedbackUpdate(feedbacks);
    		}
        });
        
        filterReservation.setOnAction((ActionEvent ev) -> { 
        	System.out.println("scarico macchine appropriate");
        	tableReservation.ListReservationUpdate(rh.showReservations(filterReservation.getValue().toString()));
        });
    }
    
    // reset the fields in the Employer Interface
    public void clearAll() {
        fieldLicensePlate.clear();
        fieldVendor.clear();
        fieldLocation.setValue("Firenze");
        fieldSeats.setValue("4");
        fieldKm.clear();
        fieldPrice.clear();
        
    }
    
    public AnchorPane getBox() {return box;}
}  
