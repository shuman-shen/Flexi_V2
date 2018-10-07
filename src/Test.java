

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainWindow;
import controller.MainWindowControl;
import model.*;

public class Test extends Application {    
    
    
    @Override
    public void start(Stage primaryStage) {
        FlexiRentSystem model = new FlexiRentSystem();
        MainWindowControl controller = new MainWindowControl(model);
        MainWindow view = new MainWindow(controller, model);

     // Create a scene and place the pane in the stage
        Scene scene = new Scene(view.asParent());
        primaryStage.setWidth(820);
        primaryStage.setHeight(500);
        primaryStage.setTitle("Flexi Rent System"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }

    public static void main(String[] args) {
        launch(args);
        
//        FlexiRentSystem f = new FlexiRentSystem();
//        f.getMainList();
//        System.out.println(f.getPropertyList().get(0).getImage());
    }
    
    
    
    
}
