package view;

import java.io.File;
import java.nio.file.Paths;

import controller.MainWindowControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import view.MainWindow.addPropertyListener;

public class AddPropertyListener implements EventHandler<ActionEvent> {

    private MainWindowControl mainControl;
    
    public AddPropertyListener(MainWindowControl mainControl) {
        this.mainControl = mainControl;
    }
    
    @Override 
    public void handle(ActionEvent e) {
        
        
        GridPane root = new GridPane();
        Scene newScene = new Scene(root);
        Stage addPropertyStage = new Stage();
        
        root.setGridLinesVisible(true);
        
        root.setPadding(new Insets(20));
        root.setHgap(10);
        root.setVgap(10);
        ColumnConstraints column1 = new ColumnConstraints(200);
        ColumnConstraints column2 = new ColumnConstraints(300);
        
        root.getColumnConstraints().addAll(column1, column2);
        
        root.setAlignment(Pos.CENTER);             
     

        // Place nodes in the pane
        
        //TODO: Exception for invalid URL
        Image propertyImg = new Image("/view/images/No_Image_Available.png");
        ImageView displayImg = new ImageView();
        displayImg.setImage(propertyImg);
        displayImg.setFitWidth(240);
        displayImg.setFitHeight(160);
        root.add(displayImg, 1, 0);
        root.setHalignment(displayImg, HPos.CENTER);
        
        root.add(new Label("Image"), 0, 1);
        
        FileChooser imgChooser = new FileChooser();
        imgChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpeg", "*.jpg", "*.gif", "*.bmp"));
        //TODO: Exception for invalid URL
        String imgPath = Paths.get("./src/view/images/").toAbsolutePath().normalize().toString();
        File path = new File(imgPath);
        if(path.exists()) imgChooser.setInitialDirectory(new File(imgPath));
        
        
        
        Button imgBtn = new Button("Select image");
        imgBtn.setOnAction(event -> {
            try {
            File newPath = imgChooser.showOpenDialog(addPropertyStage);
            if(newPath != null) {                      
            Image newImg = new Image(newPath.toURI().toURL().toExternalForm());
            displayImg.setImage(newImg);}
            }
            catch (Exception ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Alert");
         
                // alert.setHeaderText("Results:");
                alert.setContentText("Image path invalid!");
         
                alert.show();
            }
        });
        
        root.add(imgBtn, 1, 1);
        
        root.add(new Label("Bedroom Number"), 0, 6);
        
        ComboBox<Integer> numBox = new ComboBox<>();
        numBox.getItems().addAll(1, 2, 3);
        //TODO PREMIUM SUITE = 3 and not-editable
        numBox.setValue(1);                           
        root.add(numBox, 1, 6);
        
        root.add(new Label("Last Maintenance Date"), 0, 7);
        
        Label disableLabel = new Label("Not Required");
        disableLabel.setPrefSize(100, 27);
        disableLabel.setAlignment(Pos.CENTER);
        
        DatePicker date = new DatePicker();
        
        root.add(disableLabel, 1, 7);
        
        root.add(new Label("Property Type"), 0, 2);
        
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Apartment", "Premium Suite");
        typeBox.setValue("Apartment");             
        root.add(typeBox, 1, 2);
        typeBox.valueProperty().addListener(new ChangeListener<String>() {            

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // TODO Auto-generated method stub
                if(typeBox.getValue().equals("Premium Suite")) {
                    numBox.setValue(3);
                    numBox.setDisable(true);
                    root.getChildren().remove(disableLabel);
                    root.add(date, 1, 7);                     
                
                }
                else {numBox.setValue(1);
                    numBox.setDisable(false);
                    root.getChildren().remove(date);
                    root.add(disableLabel, 1, 7);                                                            
                }
            }                                 
        });
        
        root.add(new Label("Street Number"), 0, 3);
        TextField streetNum = new TextField();
        //streetNum.setPrefWidth(100);
        streetNum.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    streetNum.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        root.add(streetNum, 1, 3);
        
        root.add(new Label("Street Name"), 0, 4);
        TextField streetName = new TextField();
        root.add(streetName, 1, 4);
        
        root.add(new Label("Suburb"), 0, 5);
        TextField suburb = new TextField();
        root.add(suburb, 1, 5);
        
        root.add(new Label("Description"), 0, 8);
        TextArea desc = new TextArea();
        desc.setPrefSize(300, 150);
        desc.setWrapText(true);
        root.add(desc, 1, 8);
                               
        Button btAdd = new Button("Add Property");
        root.add(btAdd, 1, 9);
                   
           
        addPropertyStage.setWidth(600);
        addPropertyStage.setHeight(600);
        addPropertyStage.setTitle("Add New Property"); // Set the stage title
        addPropertyStage.setScene(newScene); // Place the scene in the stage
        addPropertyStage.show(); // Display the stage
        
    }
    
    
    
}

