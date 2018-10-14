package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import controller.MainWindowControl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImportListener implements EventHandler<ActionEvent>{
    
   private Stage stage;
   private MainWindowControl mainControl;
   
   public ImportListener(MainWindowControl mainControl) {
       this.mainControl = mainControl;
   }
   
    
    @Override
    public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files, *.txt", "*.txt"));
        stage = new Stage();
        //stage.show();
        
        //TODO: Exception for invalid URL
        
        String folderPath = Paths.get("./myFiles/").toAbsolutePath().normalize().toString();
        File path = new File(folderPath);
        if(path.exists()) fileChooser.setInitialDirectory(new File(folderPath));
        
        
        //try {
            File newPath = fileChooser.showOpenDialog(stage);
            if(newPath != null) {                      
            String input = newPath.getAbsolutePath().toString();
            System.out.println(folderPath); 
            //String input = folderPath+"/import_data.txt";
            System.out.println(input);
            
            mainControl.importData(input);
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Import Data");
            alert.setHeaderText(null);
            alert.setContentText("Import done.");

            alert.showAndWait();        
            
            }
            
            //}
//            catch (Exception ex) {
//                Alert alert = new Alert(AlertType.ERROR);
//                alert.setTitle("Alert");
//         
//                // alert.setHeaderText("Results:");
//                alert.setContentText("Path invalid! - export");
//         
//                alert.show();
//            }
        }
   
    
   
    
}
