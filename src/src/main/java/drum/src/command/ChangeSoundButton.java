package drum.src.command;

import drum.src.sound.SoundFactory;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import java.util.Optional;


/**
 * ChangeSoundButton is a UI component for changing the sound of all buttons in a specified row.
 * When clicked, it presents the user with a list of available sounds to choose from,
 * and upon selection, updates the sound accordingly using the ChangeSoundCommand.
 */



public class ChangeSoundButton extends Button {
    private ChangeSoundCommand command;

    public ChangeSoundButton(String buttonText, ChangeSoundCommand command, Window owner) {
        super(buttonText);
        this.command = command;
        this.setOnAction(e -> onClick(owner));
    }

    public void onClick(Window owner) {
        ComboBox<String> soundOptions = new ComboBox<>();
        soundOptions.getItems().addAll(SoundFactory.getAllSoundNames());
        VBox content = new VBox(soundOptions);
        Optional<ButtonType> result = CommandDialog.showAlert(owner, Alert.AlertType.NONE, "Select New Sound", null, null, content, ButtonType.OK, ButtonType.CANCEL);
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String selectedSound = soundOptions.getValue();
            if (selectedSound != null) {
                this.command.setNewSoundName(selectedSound);
                this.command.execute();
            }
        }
    }
}
