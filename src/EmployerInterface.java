import javafx.collections.*;
import java.lang.*;

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
    
    private final static int	CAR_MANAGER = 1,
    							USER_TABLE = 2,
    							FEEDBACK_TABLE = 3;
    Car selectedCar = null;
    
//	------ LABELS ------
    private final Label title;
    private final Text errorMsgInsertion;
    private final Text errorMsgDeletion;
    private final Label insertTitle;
    final Label licensePlate,vendor,seatNumber,location,kilometers,price;
    private final Label feedbackTitle;
    private final Label userTitle;
    private final Label carTitle;
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
//	------ TABLES ------
    private final VisualTableFeedback tableFeedback;
    private final VisualTableUser tableUser;
    private final VisualTableCar tableCar;
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
    private final AnchorPane box;
    
    private int table = CAR_MANAGER;
     
    public EmployerInterface() {
//    	------ LABELS ------
        title = new Label("Employer Management");
        errorMsgInsertion = new Text();
        errorMsgDeletion = new Text();
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
        				"Car Manager", "User Table", "Feedback Table");
        tableChoose = new ComboBox(choose);
        tableChoose.setValue("Car Manager");
        ObservableList<String> filter =
        		FXCollections.observableArrayList (
        				"1", "2", "3", "4", "5");
        filterFeedback = new ComboBox(filter);
        filterFeedback.setValue("5");
        
        		
//    	------ TABLES ------
        tableFeedback = new VisualTableFeedback();
        tableUser = new VisualTableUser(); 
        tableCar = new VisualTableCar(false);
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
        feedbackPanel.getChildren().addAll(feedbackTitle, tableFeedback, filterFeedback);
        userPanel = new VBox(DX_PANEL_SPACE); 
        userPanel.getChildren().addAll(userTitle, tableUser);
        carPanel = new VBox(DX_PANEL_SPACE);
        carPanel.getChildren().addAll(carTitle, errorMsgDeletion, tableCar, deleteButton);
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
//    	------ TABLES ------
        tableFeedback.setTableFeedbackStyle();
        tableUser.setTableUserStyle(); //tabella User
        tableCar.setTableCarStyle();
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
        
        /*feedbackPanel.setLayoutX(400);
        feedbackPanel.setLayoutY(100);
        feedbackPanel.setPrefSize(500,450);
        
        feedbackPanel.fillWidthProperty();
        
        userPanel.setLayoutX(400);
        userPanel.setLayoutY(100);
        userPanel.setPrefSize(500,450);
        
        userPanel.fillWidthProperty();
        
        carPanel.setLayoutX(400);
        carPanel.setLayoutY(100);
        carPanel.setPrefSize(500,450);
        
        carPanel.fillWidthProperty();*/
        
        box.setStyle("-fx-background-color: khaki"); 
        box.setPadding(new Insets(0,5,0,0));
        box.setPrefWidth(950);
        box.setPrefHeight(660);
        box.setTopAnchor(insertPanel,129.0);
        /*box.setLeftAnchor(title, 90.0);
        box.setTopAnchor(title, 30.0);
        box.setLeftAnchor(logOutButton,430.0);
        box.setTopAnchor(logOutButton,5.0);
        box.setBottomAnchor(deleteButton, 70.0);
        box.setBottomAnchor(insertButton, 50.0);*/
    }
    
    // listen the events 
    void empEventHandler(RentHandler rh, CarRenting carR){ 
    	// "initialization" phase
    	tableFeedback.ListFeedbackUpdate(rh.showFeedbacks());
    	tableUser.UserListUpdate(rh.showCustomers());
    	tableCar.carListUpdate(rh.showAllCars());
    	
    	// logout
        logOutButton.setOnAction((ActionEvent e)-> {
            errorMsgInsertion.setText("");
            errorMsgDeletion.setText("");
            clearAll();
            carR.setScene("logout");
        });
        
        tableChoose.setOnAction((ActionEvent ev)-> { 
        	if(tableChoose.getValue() == "User Table") {
                if(table == CAR_MANAGER) {	
                	dxPanel.getChildren().removeAll(carPanel);
                }
                else if(table == FEEDBACK_TABLE) { 
                	dxPanel.getChildren().removeAll(feedbackPanel);
                }
                table = USER_TABLE;
                dxPanel.getChildren().addAll(userPanel); 
        	}
        	
        	else if(tableChoose.getValue() == "Car Manager") {
        		if(table == USER_TABLE) {
        			dxPanel.getChildren().removeAll(userPanel);
        		}
        		else if(table == FEEDBACK_TABLE) {
        			dxPanel.getChildren().removeAll(feedbackPanel);
        		}
        		table = CAR_MANAGER;
        		tableCar.carListUpdate(rh.showAllCars());
        		dxPanel.getChildren().addAll(carPanel);
        	}
        	
        	else if(tableChoose.getValue() == "Feedback Table") {
        		if(table == CAR_MANAGER) {
        			dxPanel.getChildren().removeAll(carPanel);
        		}
        		else if(table == USER_TABLE) {
        			dxPanel.getChildren().removeAll(userPanel);
        		}
        		table = FEEDBACK_TABLE;
        		dxPanel.getChildren().addAll(feedbackPanel);
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
                    clearAll();
                } else 
                	errorMsgInsertion.setFill(Color.RED);
            } else {
            	errorMsgInsertion.setFill(Color.RED);
            	outcome = "You have to insert all the fields correctly";
            	System.out.println("Insert car form is not correct");
            }
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
        	tableFeedback.ListFeedbackUpdate(rh.showFeedbacks(Integer.parseInt(filterFeedback.getValue().toString())));
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
