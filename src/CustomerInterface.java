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
    private final AnchorPane box;
    private final VBox sxPanel; 
    private final Label title;
    private final SearchPanel searchPanel;
    private final Text userMsg;
    private final VBox dxPanel;
    private final Label tableTitle;
    private final VisualTableCar tableCar;
    private final Label rankTitle;
    private final VisualTableFeedback tableFeedback;
    private final HBox buttonBox;
    private final Button reserve;
    private final Button delete;
    private final Button logOut;

    public CustomerInterface() { 
        box = new AnchorPane();
        title = new Label("Car Renting");
        tableTitle = new Label("AVAILABLE CARS");
        sxPanel = new VBox(3);
        tableCar = new VisualTableCar();
        rankTitle = new Label("RANK");
        tableFeedback = new VisualTableFeedback();
        reserve = new Button("RESERVE");
        delete = new Button("DELETE RESERVATION");
        logOut = new Button("LOG OUT");
        buttonBox = new HBox(40);
        buttonBox.getChildren().addAll(reserve,delete);
        userMsg = new Text();
        userMsg.setFont(Font.font("Calibri", 16));
        sxPanel.getChildren().addAll(userMsg,tableTitle, tableCar, buttonBox);
        sxPanel.setAlignment(Pos.CENTER);
        searchPanel = new SearchPanel();
        dxPanel = new VBox(DX_PANEL_SPACE);
        dxPanel.getChildren().addAll(rankTitle, tableFeedback);
        box.getChildren().addAll(   logOut,title,
                                            searchPanel.getLabels(),
                                            searchPanel.getBox(),
                                            sxPanel, dxPanel  );
    }
    
    
    public void setUserInterfaceStyle() { 
        box.setStyle("-fx-background-color: aliceblue");
        box.setPrefWidth(890);
        box.setPrefHeight(660);
        box.setTopAnchor(sxPanel,129.0);
        title.setFont(Font.font("Calibri", 11 + TITLE_SIZE)); 
        title.setLayoutX(340);
        title.setLayoutY(10);
        logOut.setLayoutX(750);
        //logOut.setLayoutY(30);
        logOut.setLayoutY(3);
        tableTitle.setFont(Font.font("Calibri", 5 + SECTION_SIZE));
        //sxPanel.setMargin(tableTitle, new Insets(3,0,0,0));
        rankTitle.setFont(Font.font("Calibri", 5 + SECTION_SIZE));
        sxPanel.setLayoutX(60);
        sxPanel.setLayoutY(150);
        sxPanel.setPrefSize(400,500);
        tableCar.setTableCarStyle();
        tableFeedback.setTableFeedbackStyle();
        searchPanel.setSearchPanelStyle("Calibri", 11); 
        dxPanel.setLayoutX(530);
        dxPanel.setLayoutY(150);
        dxPanel.setPrefSize(300, 450);
        dxPanel.fillWidthProperty();
        dxPanel.setAlignment(Pos.CENTER);
 
    }
    
    void buttonBoxHandler(boolean disable) {
        reserve.setDisable(disable);
        //delete.setDisable(!disable);
    }
    
    void searchEventHandler(RentHandler rh) {
        
        searchPanel.getSearch().setOnAction((ActionEvent ev)-> {
            pickUpDate = searchPanel.getPickUpDate().getValue();
            deliveryDate = searchPanel.getDeliveryDate().getValue();
            if(deliveryDate.compareTo(pickUpDate) >= 0) {	
            	userMsg.setFill(Color.GREEN);
            	locality = searchPanel.getPlaceField().getValue().toString();
                seats = searchPanel.getSeatsNumber().getValue().toString();
                System.out.println("The car location is: "+locality);
                System.out.println("The seats of the car are: "+seats);
                StringBuilder outcome = new StringBuilder("");
                tableCar.carListUpdate(rh.showAvailableCar(pickUpDate, deliveryDate, locality, seats, outcome));
                userMsg.setText(outcome.toString());
            } else {
            	userMsg.setFill(Color.RED);
            	userMsg.setText("The delivery date has to be greater than the pick-up date");
            }
            
        });
    }
    
    public void appEventHandler(User user, RentHandler rh, CarRenting carR) {

    	
    	// "inizialization" phase
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
            //tableFeedback.ListFeedbackUpdate(rh.showFeedbacks());
        });
        
        // Listen when the customer select a car from table car
        tableCar.getSelectionModel().selectedIndexProperty().addListener((num) ->
        {           
            if(tableCar.getSelectionModel().getSelectedItem() == null) {
               tableCar.getSelectionModel().clearSelection();

            }
            else {

                selectedCar = tableCar.getSelectionModel().getSelectedItem();
                System.out.println("I selected: "+selectedCar.getIdCar());
                
                buttonBoxHandler(false);
                
                // Reserve a car
                reserve.setOnAction((ActionEvent e)-> {
	                String outcome = rh.addReservation(user, selectedCar, pickUpDate, deliveryDate);
	                if(outcome.equals("Success!")) {
	                   tableCar.getCars().remove(tableCar.getSelectionModel().getSelectedIndex());
	                   buttonBoxHandler(true);
	                   //tableFeedback.ListFeedbackUpdate(rh.showFeedbacks());
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