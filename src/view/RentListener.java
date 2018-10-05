package view;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class RentListener implements EventHandler<ActionEvent> {
    
    @Override 
    public void handle(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Property");
        dialog.setHeaderText("Search Property ID for operation");
        dialog.setContentText("Property ID: ");        
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Search");
        Optional<String> propertyID = dialog.showAndWait();
        String input = "";
        if(propertyID.isPresent()) {
            input = propertyID.get();
        }
        
        //System.out.println(input);
        
    }
    
    
}
