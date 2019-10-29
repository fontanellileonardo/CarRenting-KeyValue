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
//	------ LABELS ------
    private final Label title;
    private final Text errorMsg;
    private final Label insertTitle;
    final Label idCar,vendor,seatNumber,location,kilometers,price;
    private final Label feedbackTitle;
//	------ TEXT FIELDS ------
    private final TextField fieldIdCar; 
    private final TextField fieldVendor; 
    private final TextField fieldKm; 
    private final TextField fieldPrice;
//	------ COMBO BOXES ------
    private final ComboBox fieldLocation;
    private final ComboBox fieldSeats;
//	------ TABLES ------
    private final VisualTableFeedback tableFeedback;
//	------ BUTTONS ------
    final Button insertButton;
    final Button logOutButton;
//	------ BOXES ------
    private final VBox insertPanel;
    private final VBox dxPanel;
    private final AnchorPane box;
     
    public EmployerInterface() {
//    	------ LABELS ------
        title = new Label("Car Management");
        errorMsg = new Text();
        insertTitle = new Label("Insert a new car");
        idCar = new Label("Id Car:");
        vendor = new Label("Vendor:");
        location = new Label("Location:");
        kilometers = new Label("Kilometers:");
        price = new Label("Price:");
        feedbackTitle = new Label("FEEDBACKS");
        seatNumber = new Label("Seat Number:");
//    	------ TEXT FIELDS ------
        fieldIdCar = new TextField();
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
//    	------ TABLES ------
        tableFeedback = new VisualTableFeedback();
//    	------ BUTTONS ------
        insertButton = new Button("INSERT");
        logOutButton = new Button("LOG OUT");
//    	------ BOXES ------
        insertPanel = new VBox(3);
        insertPanel.getChildren().addAll(insertTitle,errorMsg,idCar,fieldIdCar,vendor,
                fieldVendor,seatNumber,fieldSeats, location,
                fieldLocation,kilometers, fieldKm, price, 
                fieldPrice, insertButton);
        dxPanel = new VBox(DX_PANEL_SPACE);
        dxPanel.getChildren().addAll(feedbackTitle, tableFeedback);
        box = new AnchorPane();
        box.getChildren().addAll(logOutButton,title,insertPanel,dxPanel);
    }
    
    public void setEmpInterfaceStyle() { 
//    	------ LABELS ------
        title.setFont(Font.font("Calibri", 11 + TITLE_SIZE)); 
        title.setLayoutX(100);
        title.setLayoutY(10);
        insertTitle.setFont(Font.font("Calibri", 16));
        errorMsg.setFont(Font.font("Calibri", 16));
        errorMsg.setFill(Color.RED);
//    	------ TABLES ------
        tableFeedback.setTableFeedbackStyle();
//    	------ BOXES ------
        insertPanel.setLayoutX(100);
        insertPanel.setLayoutY(50);
        insertPanel.setPrefSize(300,450);
        insertPanel.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: khaki"); 
        box.setPadding(new Insets(0,5,0,0));
        box.setLeftAnchor(title, 90.0);
        box.setTopAnchor(title, 30.0);
        box.setLeftAnchor(logOutButton,430.0);
        box.setTopAnchor(logOutButton,5.0);
    }
    
    // listen the events 
    void empEventHandler(RentHandler rh, CarRenting carR){
    	// "initialization" phase
    	tableFeedback.ListFeedbackUpdate(rh.showFeedbacks());
    	
    	// logout
        logOutButton.setOnAction((ActionEvent e)-> {
            errorMsg.setText("");
            clearAll();
            carR.setScene("logout");
        });
        
        // insert car
        insertButton.setOnAction((ActionEvent ev)-> {
        	// takes field from the form fields
            String loc = fieldLocation.getValue().toString();
            int seats = Integer.parseInt(fieldSeats.getValue().toString());
            // Check if all the fields are correct
            String outcome = "";
            if(fieldIdCar.getText().equals("") == false && fieldVendor.getText().equals("") == false &&
            		fieldKm.getText().matches("^\\d+$") == true && 
            		fieldPrice.getText().matches("^\\d+\\.?[0-9]*$") == true ) {
            	Car car = new Car(fieldVendor.getText(),seats,loc,Integer.parseInt(fieldKm.getText()),
            			Double.parseDouble(fieldPrice.getText()),fieldIdCar.getText(), false);
            	// try to insert new car in the DB
            	outcome = rh.insertCar(car);
                if(outcome.equals("Success!")) {
                	errorMsg.setFill(Color.GREEN);
                    clearAll();
                } else 
                	errorMsg.setFill(Color.RED);
            } else {
            	errorMsg.setFill(Color.RED);
            	outcome = "You have to insert all the fields correctly";
            	System.out.println("Insert car form is not correct");
            }
            errorMsg.setText(outcome);
        });
        
    }
    
    // reset the fields in the Employer Interface
    public void clearAll() {
        
        fieldIdCar.clear();
        fieldVendor.clear();
        fieldLocation.setValue("Firenze");
        fieldSeats.setValue("4");
        fieldKm.clear();
        fieldPrice.clear();
    }
    
    public AnchorPane getBox() {return box;}
}  
