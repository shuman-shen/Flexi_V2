
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import view.MainWindow;
import controller.MainWindowControl;
import model.*;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FlexiRentSystem model = new FlexiRentSystem();

            MainWindowControl controller = new MainWindowControl(model);
            MainWindow view = new MainWindow(controller);

            // Create a scene and place the pane in the stage
            Scene scene = new Scene(view.asParent());
            primaryStage.setWidth(840);
            primaryStage.setHeight(500);
            primaryStage.setTitle("Flexi Rent System"); // Set the stage title
            primaryStage.setScene(scene); // Place the scene in the stage
            primaryStage.show(); // Display the stage

        }

        catch (Exception e) {
            Alert alert2 = new Alert(AlertType.INFORMATION);
            alert2.setHeaderText("Error.");
            alert2.showAndWait();

        }
    }

    public static void main(String[] args) {
        launch(args);

    }

}
