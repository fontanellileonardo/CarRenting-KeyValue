
import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;


public class VisualTableFeedback extends TableView<Feedback> {
    private ObservableList<Feedback> feedbackList;
    
     public VisualTableFeedback(boolean customer){
    	 TableColumn columnNickname = new TableColumn("NICK NAME");
         columnNickname.setCellValueFactory(new PropertyValueFactory<>("nickName"));	
         TableColumn columnDate = new TableColumn("DATE");
         columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
         TableColumn columnMark = new TableColumn("MARK");
         columnMark.setCellValueFactory(new PropertyValueFactory<>("mark"));
         TableColumn columnComment = new TableColumn("COMMENT");
         columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    	 if(customer) {
    		 TableColumn columnFiscalCode = new TableColumn("NICK NAME");
             columnFiscalCode.setCellValueFactory(new PropertyValueFactory<>("nickName"));
             getColumns().addAll(columnFiscalCode,columnNickname, columnDate, columnMark, columnComment);
    	 } else {
    		 getColumns().addAll(columnNickname, columnDate, columnMark, columnComment);
    	 }
        feedbackList = FXCollections.observableArrayList();
        setItems(feedbackList);
    }
    
    public void setTableFeedbackStyle() {
    	setStyle("-fx-selection-bar: powderblue; -fx-column-header-background: azure;");
        //setFixedCellSize(90);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setPrefWidth(400);
        setMaxHeight(480);
    }
    
    public void ListFeedbackUpdate(List<Feedback> feedbacks){
    	feedbackList.clear();
    	feedbackList.addAll(feedbacks);
    }
    
    public ObservableList<Feedback> getFeedback() {return feedbackList;}
    
}

