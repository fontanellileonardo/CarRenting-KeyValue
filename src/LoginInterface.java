
import java.math.*;
import java.security.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;


public class LoginInterface {
    
    
    private final static int    TITLE_SIZE = 30,
                                SECTION_SIZE = 9, 
                                DX_PANEL_SPACE = 3; 
    private final AnchorPane box;
    private final Label title;
    private final Text loginMsg;
    //login section
    private final VBox loginPanel; 
    final Label loginTitle,email,password,status;
    private final TextField fieldEmail; 
    private final PasswordField fieldPwd;
    //private final TextField fieldPwd; 
    private final ComboBox fieldStatus;
    final Button login;
    
    //registration section
    private final VBox registerPanel; 
    final Label registerTitle;
    private final Text regMsg;
    final Label r_fiscalCode, r_nickName, r_name, r_surname, r_email, r_password, r_status;
    private final TextField r_fieldFiscalCode, r_fieldNickName, r_fieldName, r_fieldSurname, r_fieldEmail, r_fieldPwd;
    private final ComboBox r_fieldStatus;
    final Button submit;
    
    public LoginInterface() {
    
        box = new AnchorPane();
        title = new Label("Car Renting");
        
        //initializing login section
        loginMsg = new Text();
        loginPanel = new VBox(3);
        loginTitle = new Label("Login Form");
        email = new Label("Email:");
        fieldEmail = new TextField();
        password = new Label("Password:");
        fieldPwd = new PasswordField();
        status = new Label("Status:");
        ObservableList<String> levels = 
                FXCollections.observableArrayList (
                    "Customer","Employer");
        fieldStatus = new ComboBox(levels);
        fieldStatus.setValue("Customer");
        login = new Button("LOGIN");
        
        //inizializing registration section
        regMsg = new Text();
        registerPanel = new VBox(DX_PANEL_SPACE);
        registerTitle = new Label("Registration form");
        r_name = new Label("Name:");
        r_fieldName = new TextField();
        r_surname = new Label("Surname:");
        r_fieldSurname = new TextField();
        r_fiscalCode = new Label("Fiscal Code:");
        r_fieldFiscalCode = new TextField();
        r_nickName = new Label("Nickname:");
        r_fieldNickName = new TextField();
        r_email = new Label("Email:");
        r_fieldEmail = new TextField();
        r_password = new Label("Password:");
        r_fieldPwd = new TextField();
        r_status = new Label("Status:");
        ObservableList<String> r_levels = 
                FXCollections.observableArrayList (
                    "Customer","Employer");
        r_fieldStatus = new ComboBox(r_levels);
        r_fieldStatus.setValue("Customer");
        submit = new Button("SUBMIT");
        
        loginPanel.getChildren().addAll(loginTitle,email,fieldEmail,password,
                                        fieldPwd,status,fieldStatus, loginMsg,
                                        login);
        
        registerPanel.getChildren().addAll(registerTitle, r_name, r_fieldName, r_surname, 
                                           r_fieldSurname, r_fiscalCode, r_fieldFiscalCode, r_nickName,
                                           r_fieldNickName, r_email, r_fieldEmail, 
                                           r_password, r_fieldPwd, 
                                           r_status, r_fieldStatus, regMsg, submit);
        
        box.getChildren().addAll(title,loginPanel, registerPanel);
    }
    
    public void setLoginInterfaceStyle() { 
        box.setStyle("-fx-background-color: aliceblue");
        box.setPrefWidth(800);
        title.setFont(Font.font("Calibri", 11 + TITLE_SIZE)); 
        title.setLayoutX(275);
        title.setLayoutY(10);
        loginMsg.setFont(Font.font("Calibri", 16));
        loginMsg.setFill(Color.RED);
        regMsg.setFont(Font.font("Calibri", 16));
        regMsg.setFill(Color.RED);
        loginPanel.setLayoutX(60);
        loginPanel.setPrefSize(300,450);
        loginPanel.setAlignment(Pos.CENTER);
        registerTitle.setLayoutX(300);
        registerPanel.setLayoutX(430);
        registerPanel.setLayoutY(51);
        registerPanel.setPrefSize(300, 450);
        registerPanel.fillWidthProperty();
        registerPanel.setAlignment(Pos.CENTER);
    }
    
    String cryptPwd(String pwd) {
    	String ret = null;
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
    
    void startEventHandler(User loggedUser, RentHandler rh, CarRenting carR){
    	
        login.setOnAction((ActionEvent ev)-> {
            loggedUser.setEmail(fieldEmail.getText());
            loggedUser.setPassword(cryptPwd(fieldPwd.getText()));
            //loggedUser.setPassword(fieldPwd.getText());
            String selectedStatus = fieldStatus.getValue().toString();
            switch (selectedStatus) {
                case "Customer":
                    loggedUser.setCustomer(true);
                    break;
                case "Employer":
                    loggedUser.setCustomer(false);
                    break;
            }
            String outcome = rh.login(loggedUser);
            if(outcome.equals("Success!")) {
                loginMsg.setText("");
                clearAll();
                regMsg.setText("");
                carR.setScene(selectedStatus);    
            }
            else
                loginMsg.setText(outcome);
        });
        
        submit.setOnAction((ActionEvent ev)-> {
            User regUser = null;  
            String selectedStatus = r_fieldStatus.getValue().toString();
            switch (selectedStatus) {
                case "Customer":
                    regUser = new User(r_fieldFiscalCode.getText(), r_fieldNickName.getText(), r_fieldName.getText(), r_fieldSurname.getText(), true,
                                    r_fieldEmail.getText(),cryptPwd(r_fieldPwd.getText()));
                    break;
                case "Employer":
                    regUser = new User(r_fieldFiscalCode.getText(), r_fieldNickName.getText(), r_fieldName.getText(),r_fieldSurname.getText(), false,
                                    r_fieldEmail.getText(),cryptPwd(r_fieldPwd.getText()));
                    break;
            }
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
    
    public void clearAll() {
        fieldEmail.clear();
        fieldPwd.clear();
        fieldStatus.setValue("Customer");
        r_fieldName.clear();
        r_fieldSurname.clear();
        r_fieldEmail.clear();
        r_fieldPwd.clear();
        r_fieldStatus.setValue("Customer");
    }
    
    public AnchorPane getBox() {return box;}
}  
