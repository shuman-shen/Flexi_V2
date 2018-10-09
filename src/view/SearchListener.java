package view;

import java.util.Optional;

import controller.MainWindowControl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SearchListener implements EventHandler<ActionEvent> {
    
    private MainWindowControl mainControl;
    
    public SearchListener(MainWindowControl mainControl) {
        // TODO Auto-generated constructor stub
        this.mainControl = mainControl;
    }

    @Override 
    public void handle(ActionEvent e) {
//        TextInputDialog dialog = new TextInputDialog();
//        dialog.setTitle("Search Property");
//        dialog.setHeaderText("Search Property ID for operation");
//        dialog.setContentText("Property ID: ");        
//        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Search");
//        Optional<String> propertyID = dialog.showAndWait();
//        String input = "";
//        Button search = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
//        if(propertyID.isPresent()) {
//            input = propertyID.get();
//            System.out.println("Search" + input);
//            search.setOnAction(new EventHandler<ActionEvent>() {
//
//                @Override
//                public void handle(ActionEvent event) {
//                    // TODO Auto-generated method stub
//                    Text t = new Text("TEST");
//                    VBox v = new VBox();
//                    v.getChildren().add(t);
//                    
//                    Scene s = new Scene(v);
//                    Stage ss = new Stage();
//                    ss.setWidth(80);
//                    ss.setHeight(60);
//                    ss.setTitle("Property details"); // Set the stage title
//                    ss.setScene(s); // Place the scene in the stage
//                    
//                    ss.show(); // Display the stage
//                    
//                    
//                }
//                
//            });
        //}
        
        
        
        //if(mainControl.getFilteredList());
        
        //System.out.println(input);
        
    }
    
    
}
