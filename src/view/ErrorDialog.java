package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ErrorDialog {

    private String msg;

    public ErrorDialog(String msg) {
        this.msg = msg;
    }

    public void triggerAlert() {

        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(msg);

        alert.showAndWait();
    }

}
