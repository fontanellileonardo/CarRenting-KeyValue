
import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;


public class VisualTableRank extends TableView<User> {
    private ObservableList<User> ScoreList;
    
     public VisualTableRank(){
       
        TableColumn columnName = new TableColumn("NAME");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn columnSurname = new TableColumn("SURNAME");
        columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        TableColumn columnScore = new TableColumn("SCORE");
        columnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        ScoreList = FXCollections.observableArrayList();
        setItems(ScoreList);
        getColumns().addAll(columnName, columnSurname, columnScore);
    }
    
    public void setTableRankStyle() {
        setFixedCellSize(40);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setPrefWidth(90);
        setMaxHeight(480);
    }
    
    public void ListRankUpdate(List<User> scores){
        ScoreList.clear();
        ScoreList.addAll(scores);
    }
    
    public ObservableList<User> getRank() {return ScoreList;}
    
}

