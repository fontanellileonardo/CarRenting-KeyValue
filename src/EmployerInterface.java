
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
    private final AnchorPane box;
    private final Label title;
    private final Text errorMsg;
    private final Label insertTitle;
    //login section
    private final VBox insertPanel; 
    final Label idCar,vendor,seatNumber,location,kilometers,price;
    private final TextField fieldIdCar; 
    private final TextField fieldVendor; 
    private final ComboBox fieldLocation;
    private final ComboBox fieldSeats;
    private final TextField fieldKm; 
    private final TextField fieldPrice; 
    final Button insertButton;
    final Button logOutButton;
    private final VBox dxPanel;
    private final Label feedbackTitle;
    private final VisualTableFeedback tableFeedback;
    
    public EmployerInterface() {
    
        box = new AnchorPane();
        title = new Label("Car Management");
        
        //initializing login section
        errorMsg = new Text();
        insertPanel = new VBox(3);
        insertTitle = new Label("Insert a new car");
        idCar = new Label("Id Car:");
        fieldIdCar = new TextField();
        vendor = new Label("Vendor:");
        fieldVendor = new TextField();
        location = new Label("Location:");
        ObservableList<String> loc = 
                FXCollections.observableArrayList (
                    "Firenze","Pisa");
        fieldLocation = new ComboBox(loc);
        fieldLocation.setValue("Firenze");
        seatNumber = new Label("Seat Number:");
        ObservableList<String> seats = 
                FXCollections.observableArrayList (
                    "2","4","5","6");
        fieldSeats = new ComboBox(seats);
        fieldSeats.setValue("4");
        kilometers = new Label("Kilometers:");
        fieldKm = new TextField();
        price = new Label("Price:");
        fieldPrice = new TextField();
        insertButton = new Button("INSERT");
        logOutButton = new Button("LOG OUT");
        feedbackTitle = new Label("FEEDBACKS");
        tableFeedback = new VisualTableFeedback();
        dxPanel = new VBox(DX_PANEL_SPACE);
        dxPanel.getChildren().addAll(feedbackTitle, tableFeedback);
        insertPanel.getChildren().addAll(insertTitle,errorMsg,idCar,fieldIdCar,vendor,
                                        fieldVendor,seatNumber,fieldSeats, location,
                                        fieldLocation,kilometers, fieldKm, price, 
                                        fieldPrice, insertButton);
        
        box.getChildren().addAll(logOutButton,title,insertPanel,dxPanel);
    }
    
    public void setEmpInterfaceStyle() { 
        box.setStyle("-fx-background-color: khaki"); 
        box.setPadding(new Insets(0,5,0,0));
        title.setFont(Font.font("Calibri", 11 + TITLE_SIZE)); 
        title.setLayoutX(100);
        title.setLayoutY(10);
        errorMsg.setFont(Font.font("Calibri", 16));
        errorMsg.setFill(Color.RED);
        insertTitle.setFont(Font.font("Calibri", 16));
        insertPanel.setLayoutX(100);
        insertPanel.setLayoutY(50);
        insertPanel.setPrefSize(300,450);
        insertPanel.setAlignment(Pos.CENTER);
        tableFeedback.setTableFeedbackStyle();
        box.setLeftAnchor(title, 90.0);
        box.setTopAnchor(title, 30.0);
        box.setLeftAnchor(logOutButton,430.0);
        box.setTopAnchor(logOutButton,5.0);
    }
    
    void empEventHandler(RentHandler rh, CarRenting carR){
    	tableFeedback.ListFeedbackUpdate(rh.showFeedbacks());
    	
        logOutButton.setOnAction((ActionEvent e)-> {
            errorMsg.setText("");
            clearAll();
            carR.setScene("logout");
        });
        
        insertButton.setOnAction((ActionEvent ev)-> {
            String loc = fieldLocation.getValue().toString();
            int seats = Integer.parseInt(fieldSeats.getValue().toString());
            // Check if all the fields are correct
            String outcome = "";
            if(fieldIdCar.getText().equals("") == false && fieldVendor.getText().equals("") == false &&
            		fieldKm.getText().matches("^\\d+$") == true && 
            		fieldPrice.getText().matches("^\\d+\\.?[0-9]*$") == true ) {
            	Car car = new Car(fieldVendor.getText(),seats,loc,Integer.parseInt(fieldKm.getText()),
            			Double.parseDouble(fieldPrice.getText()),fieldIdCar.getText(), false);
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
