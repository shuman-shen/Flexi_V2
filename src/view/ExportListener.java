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

public class ExportListener implements EventHandler<ActionEvent>{
    
   private Stage stage;
   private MainWindowControl mainControl;
   
   public ExportListener(MainWindowControl mainControl) {
       this.mainControl = mainControl;
   }
   
    
    @Override
    public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        DirectoryChooser folderChooser = new DirectoryChooser();
        stage = new Stage();
        //stage.show();
        
        //TODO: Exception for invalid URL
        String folderPath = Paths.get("./myFiles/").toAbsolutePath().normalize().toString();
        File path = new File(folderPath);
        
        if(path.exists()) {
            folderChooser.setInitialDirectory(path);
        
        
        //try {
            File newPath = folderChooser.showDialog(stage);
            if(newPath != null) {                      
            folderPath = newPath.getAbsolutePath().toString();
            System.out.println(folderPath); 
            String output = folderPath+"/export_data.txt";
            System.out.println(output);
            
            mainControl.exportData(output);
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Export Data");
            alert.setHeaderText(null);
            alert.setContentText("Export done.");

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
    
}
