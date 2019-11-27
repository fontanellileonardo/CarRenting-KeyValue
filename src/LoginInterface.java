import java.math.*;
import java.security.*;
import java.util.Calendar;

import org.jboss.jandex.Main;

import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;


public class LoginInterface {
    
    
    private final static int    TITLE_SIZE = 30,
                                SECTION_SIZE = 9, 
                                DX_PANEL_SPACE = 3; 
//	------ LABELS ------
    private final Label title;
    // msg for the user(log form). Green->everything ok, Red->errors
    private final Text loginMsg;
    private final Label loginTitle,email,password,status;
    private final Label registerTitle;
    // msg for the user(reg form). Green->everything ok, Red->errors
    private final Text regMsg;
    private final Label r_fiscalCode, r_nickName, r_name, r_surname, r_email, r_password, r_status;
//	------ TEXT FIELDS ------  
    private final TextField l_fieldEmail; 
    private final PasswordField l_fieldPwd;
    private final TextField r_fieldFiscalCode, r_fieldNickName, r_fieldName, r_fieldSurname, r_fieldEmail, r_fieldPwd;
//	------ COMBO BOXES ------
    private final ComboBox fieldStatus;
    private final ComboBox r_fieldStatus;
//	------ BUTTONS ------
    private final Button login;
    final Button submit;
//	------ IMAGE ------
    private final Image carImage;
    private final ImageView carImageView;
//	------ BOXES ------
    private final VBox loginPanel; 
    private final VBox registerPanel; 
    private final AnchorPane box;
    
    public LoginInterface() {
//    	------ LABELS ------
        title = new Label("Car Renting");
        loginTitle = new Label("Login Form");
        loginMsg = new Text();
        email = new Label("Email:");
        password = new Label("Password:");
        status = new Label("Status:");
        regMsg = new Text();
        registerTitle = new Label("Registration form");
        r_name = new Label("Name:");
        r_surname = new Label("Surname:");
        r_fiscalCode = new Label("Fiscal Code:");
        r_nickName = new Label("Nickname:");
        r_email = new Label("Email:");
        r_password = new Label("Password:");
        r_status = new Label("Status:");
//    	------ TEXT FIELDS AND COMBO BOXES ------ 
        l_fieldEmail = new TextField();
        l_fieldPwd = new PasswordField();
        r_fieldName = new TextField();
        r_fieldSurname = new TextField();
        r_fieldFiscalCode = new TextField();
        r_fieldNickName = new TextField();
        r_fieldEmail = new TextField();
        r_fieldPwd = new TextField();
//    	------ COMBO BOXES ------
        ObservableList<String> levels = 
                FXCollections.observableArrayList (
                    "Customer","Employer");
        fieldStatus = new ComboBox(levels);
        fieldStatus.setValue("Customer");
        r_fieldStatus = new ComboBox(levels);
        r_fieldStatus.setValue("Customer");
//    	------ BUTTONS ------
        login = new Button("LOGIN");
        submit = new Button("SUBMIT");
//      ------ IMAGE ------
        carImage = new Image("resources/icon_rental_car.png");
        carImageView = new ImageView();
        carImageView.setImage(carImage);
        carImageView.setFitWidth(100);
        carImageView.setPreserveRatio(true);
        carImageView.setSmooth(true);
        carImageView.setCache(true);
//    	------ BOXES ------
        loginPanel = new VBox(3);
        loginPanel.getChildren().addAll(loginTitle,email,l_fieldEmail,password,
                l_fieldPwd,status,fieldStatus, loginMsg,
                login);
        registerPanel = new VBox(DX_PANEL_SPACE);
        registerPanel.getChildren().addAll(registerTitle, r_name, r_fieldName, r_surname, 
                r_fieldSurname, r_fiscalCode, r_fieldFiscalCode, r_nickName,
                r_fieldNickName, r_email, r_fieldEmail, 
                r_password, r_fieldPwd, 
                r_status, r_fieldStatus, regMsg, submit);
        box = new AnchorPane();        
        box.getChildren().addAll(carImageView,title,loginPanel, registerPanel);
    }
    
    // set the style
    public void setLoginInterfaceStyle() { 
//    	------ LABELS ------
        title.setFont(Font.font("Calibri", 11 + TITLE_SIZE)); 
        title.setLayoutX(280);
        title.setLayoutY(15);
        loginMsg.setFont(Font.font("Calibri", 16));
        loginMsg.setFill(Color.RED);
        regMsg.setFont(Font.font("Calibri", 16));
        regMsg.setFill(Color.RED);
        registerTitle.setLayoutX(300);
//    	------ BOXES ------
        loginPanel.setLayoutX(60);
        loginPanel.setPrefSize(300,450);
        loginPanel.setAlignment(Pos.CENTER);
        registerPanel.setLayoutX(430);
        registerPanel.setLayoutY(51);
        registerPanel.setPrefSize(300, 450);
        registerPanel.fillWidthProperty();
        registerPanel.setAlignment(Pos.CENTER);
        box.setLeftAnchor(carImageView,200.0);
        box.setStyle("-fx-background-color: aliceblue");
        box.setPrefWidth(800);
    }
    
    // encrypt the password
    String cryptPwd(String pwd) {
    	String ret = null;
    	// if the field is empty -> error
    	if(pwd.equals("") == true)
    		return ret;
    	try {
    		MessageDigest digest = MessageDigest.getInstance("SHA-1");
    		digest.reset();
    		digest.update(pwd.getBytes("utf-8"));
    		ret = String.format("%040x",new BigInteger(1,digest.digest()));
    	} catch(Exception e) {
    		System.out.println("Exception in method cryptoPwd"+e.getMessage());
    	}
    	return ret;
    }
    
    // listen the events from Login Interface
    void startEventHandler(User loggedUser, RentHandler rh, CarRenting carR){
    	// login
        login.setOnAction((ActionEvent ev)-> {
        	// takes values from the log form
            loggedUser.setEmail(l_fieldEmail.getText().trim());
            loggedUser.setPassword(cryptPwd(l_fieldPwd.getText().trim()));
            String selectedStatus = fieldStatus.getValue().toString();
            switch (selectedStatus) {
                case "Customer":
                    loggedUser.setCustomer(true);
                    break;
                case "Employer":
                    loggedUser.setCustomer(false);
                    break;
            }
            // check if the credentials are right
            String outcome = rh.login(loggedUser);
            System.out.println("Utente aggiornato: "+loggedUser.getFiscalCode());
            if(outcome.equals("Success!")) {
                loginMsg.setText("");
                clearAll();
                regMsg.setText("");
                // change the scene
                carR.setScene(selectedStatus);    
            }
            else
                loginMsg.setText(outcome);
        });
        
        // registration
        submit.setOnAction((ActionEvent ev)-> {
            User regUser = null;  
            // takes values from reg form
            String selectedStatus = r_fieldStatus.getValue().toString();
            switch (selectedStatus) {
                case "Customer":
                    regUser = new User(r_fieldFiscalCode.getText().trim(), r_fieldNickName.getText().trim(), r_fieldName.getText().trim(), r_fieldSurname.getText().trim(), true,
                                    r_fieldEmail.getText().trim(),cryptPwd(r_fieldPwd.getText().trim()));
                    System.out.println("Campi form: "+" fc:"+r_fieldFiscalCode.getText().trim()+" nickn: "+r_fieldNickName.getText().trim()+" name:"+r_fieldName.getText().trim()+" surname: "+r_fieldSurname.getText().trim()
                                    +" email: "+r_fieldEmail.getText().trim()+" pwd: "+cryptPwd(r_fieldPwd.getText().trim()));
                    break;
                case "Employer":
                    regUser = new User(r_fieldFiscalCode.getText().trim(), r_fieldNickName.getText().trim(), r_fieldName.getText().trim(),r_fieldSurname.getText().trim(), false,
                                    r_fieldEmail.getText().trim(),cryptPwd(r_fieldPwd.getText().trim()));
                    break;
            }
            // try to insert the user in the DB
            String outcome = rh.register(regUser);
            if(outcome.equals("You've been registered!")) {
                regMsg.setFill(Color.GREEN);
                clearAll();
            }
            else
                regMsg.setFill(Color.RED);
            regMsg.setText(outcome);
        });
    }
    
    // reset the fields in the Login Interface
    public void clearAll() {
        l_fieldEmail.clear();
        l_fieldPwd.clear();
        fieldStatus.setValue("Customer");
        r_fieldFiscalCode.clear();
        r_fieldNickName.clear();
        r_fieldName.clear();
        r_fieldSurname.clear();
        r_fieldEmail.clear();
        r_fieldPwd.clear();
        r_fieldStatus.setValue("Customer");
    }
    
    public AnchorPane getBox() {return box;}
}  
