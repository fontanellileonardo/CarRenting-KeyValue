import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

public class VisualTableUser extends TableView<User>{
	private ObservableList<User> userList;
	
    public VisualTableUser(){
        TableColumn columnFiscalCode = new TableColumn("FISCAL CODE");
        columnFiscalCode.setCellValueFactory(new PropertyValueFactory<>("fiscalCode"));
        TableColumn columnNickname = new TableColumn("NICKNAME");
        columnNickname.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        TableColumn columnName = new TableColumn("NAME");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn columnSurname = new TableColumn("SURNAME");
        columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        TableColumn columnEmail = new TableColumn("EMAIL");
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        userList = FXCollections.observableArrayList();
        setItems(userList);
        getColumns().addAll(columnFiscalCode,columnNickname,columnName, columnSurname, columnEmail);
    }
	
    public void setTableUserStyle() { //da sistemare
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setPrefWidth(90);
        setMaxHeight(480);
    }
    
    
    public void UserListUpdate(List<User> users){
    	userList.clear();
    	userList.addAll(users);
    }

    public ObservableList<User> getUsers() {return userList;}
    
}
