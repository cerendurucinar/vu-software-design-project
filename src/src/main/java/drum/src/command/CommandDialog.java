package drum.src.command;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.Optional;

/**
 * This class aims to simplify the creation and display
 * of alert dialogs within a JavaFX application.
 */
public class CommandDialog {

    /**
     * Displays a customizable alert dialog
     *
     * @param owner The owner window of the alert dialog
     * @param alertType The type of alert (e.g., INFORMATION, WARNING, ERROR)
     * @param title The text title of the alert dialog window
     * @param headerText The header text displayed at the top of the alert content
     * @param contentText The main text content of the alert
     * @param customContent A Node object for inclusion of a custom graphical element within the dialog
     * @param buttonTypes An array of ButtonType objects that will be displayed in the alert. As default "OK" button is used
     * @return An Optional containing the ButtonType that was clicked to close the dialog
     */

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
