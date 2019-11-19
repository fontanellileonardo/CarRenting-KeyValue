
import javafx.application.Application;
//import javafx.event.*;
//import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class CarRenting extends Application {
    
    private RentHandler rentHandler;
    private User loggedUser = new User();
    private CustomerInterface graphicInterface;
    private LoginInterface loginInterface;
    private EmployerInterface empInterface;
    private Stage stage;
    private Scene sceneStart;
    private Scene sceneEmployer;
    private Scene sceneCustomer;
    
    
    public void start(Stage stage) {
        this.stage = stage;
        rentHandler = new RentHandler();
        loginInterface = new LoginInterface();
        empInterface = new EmployerInterface();
        loginInterface.setLoginInterfaceStyle();
        loginInterface.startEventHandler(loggedUser, rentHandler, this);
        empInterface.setEmpInterfaceStyle();
        graphicInterface = new CustomerInterface();
        graphicInterface.setUserInterfaceStyle();
        
        sceneStart = new Scene(new Group(loginInterface.getBox()));
        sceneEmployer = new Scene(new Group(empInterface.getBox()));
        sceneCustomer = new Scene(new Group(graphicInterface.getBox()));
        
        this.stage.setTitle("Car Renting");
        this.stage.setScene(sceneStart);
        //this.stage.setScene(sceneEmployer);
        this.stage.setOnCloseRequest((WindowEvent we) -> {
        	rentHandler.closeConnections();
        });
        this.stage.show();
    }

    public void setScene(String type){
        switch (type){
            case "Customer":
                graphicInterface.appEventHandler(loggedUser, rentHandler, this);
                this.stage.setScene(sceneCustomer);
                this.stage.show();
                break;
            case "Employer":
            	empInterface.empEventHandler(rentHandler, this);
                this.stage.setScene(sceneEmployer);
                this.stage.show();
                break;
            default:
                this.stage.setScene(sceneStart);
                this.stage.show();
                break;
        }
    }
}
