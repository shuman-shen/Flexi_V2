package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
//import view.MainWindow.addPropertyListener;

public class AddPropertyListener implements EventHandler<ActionEvent> {

//    private MenuItem menuItem;
//    
//    public AddPropertyListener(MenuItem menuItem) {
//        this.menuItem = menuItem;
//    }
    
    @Override 
    public void handle(ActionEvent e) {
        
        GridPane root = new GridPane();
        root.setGridLinesVisible(true);
        
        root.setPadding(new Insets(20));
        root.setHgap(10);
        root.setVgap(10);
        ColumnConstraints column1 = new ColumnConstraints(200);
        ColumnConstraints column2 = new ColumnConstraints(200);
        
        root.getColumnConstraints().addAll(column1, column2);
        
        root.setAlignment(Pos.CENTER);             
     

        // Place nodes in the pane
        root.add(new Label("Property ID"), 0, 0);
        root.add(new TextField("TO BE AUTO GENERATED LATER ON"), 1, 0);
        
        root.add(new Label("Bedroom Number"), 0, 5);
        
        ComboBox<Integer> numBox = new ComboBox<>();
        numBox.getItems().addAll(1, 2, 3);
        //TODO PREMIUM SUITE = 3 and not-editable
        numBox.setValue(1);                           
        root.add(numBox, 1, 5);
        
        root.add(new Label("Last Maintenance Date"), 0, 6);
        
        Label disableLabel = new Label("Not available");
        disableLabel.setPrefSize(100, 27);
        disableLabel.setAlignment(Pos.CENTER);
        
        DatePicker date = new DatePicker();
        
        root.add(disableLabel, 1, 6);
        
        root.add(new Label("Property Type"), 0, 1);
        
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Apartment", "Premium Suite");
        typeBox.setValue("Apartment");             
        root.add(typeBox, 1, 1);
        typeBox.valueProperty().addListener(new ChangeListener<String>() {            

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // TODO Auto-generated method stub
                if(typeBox.getValue().equals("Premium Suite")) {
                    numBox.setValue(3);
                    numBox.setDisable(true);
                    root.getChildren().remove(disableLabel);
                    root.add(date, 1, 6);                     
                
                }
                else {numBox.setValue(1);
                    numBox.setDisable(false);
                    root.getChildren().remove(date);
                    root.add(disableLabel, 1, 6);                                                            
                }
            }                                 
        });
        
        root.add(new Label("Street Number"), 0, 2);
        TextField streetNum = new TextField();           
        root.add(streetNum, 1, 2);
        
        root.add(new Label("Street Name"), 0, 3);
        TextField streetName = new TextField();
        root.add(streetName, 1, 3);
        
        root.add(new Label("Suburb"), 0, 4);
        TextField suburb = new TextField();
        root.add(suburb, 1, 4);
                               
        Button btAdd = new Button("Add Property");
        root.add(btAdd, 1, 7);
                   
        Scene newScene = new Scene(root);
        Stage addPropertyStage = new Stage();   
        addPropertyStage.setWidth(460);
        addPropertyStage.setHeight(500);
        addPropertyStage.setTitle("Add New Property"); // Set the stage title
        addPropertyStage.setScene(newScene); // Place the scene in the stage
        addPropertyStage.show(); // Display the stage
        
    }
    
}

