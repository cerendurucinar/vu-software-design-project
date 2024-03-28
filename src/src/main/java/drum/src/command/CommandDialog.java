package drum.src.command;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.Optional;

public class CommandDialog {
    public static Optional<ButtonType> showAlert(Window owner, Alert.AlertType alertType, String title, String headerText, String contentText, Node customContent, ButtonType... buttonTypes) {
        Alert alert = new Alert(alertType);
        alert.initOwner(owner);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        if (customContent != null) {
            alert.getDialogPane().setContent(customContent);
        }

        if (buttonTypes.length > 0) {
            alert.getButtonTypes().setAll(buttonTypes);
        } else {
            alert.getButtonTypes().setAll(ButtonType.OK);
        }

        return alert.showAndWait();
    }
}
