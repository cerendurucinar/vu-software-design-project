package drum.src.ui;

import drum.src.command.ChangeSoundCommand;
import drum.src.command.Command;
import drum.src.drumsequencer.DrumSequence;
import drum.src.drumsequencer.DrumSequencer;
import drum.src.observer.Subject;
import drum.src.sound.Sound;
import drum.src.sound.SoundFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.List;
import java.util.Optional;


public class ChangeSoundButton extends Button {
    private ChangeSoundCommand command;

    public ChangeSoundButton(String buttonText, ChangeSoundCommand command, Window owner) {
        super(buttonText); // Use "super" to set the button text, leveraging inheritance
        this.command = command;
        this.setOnAction(e -> onClick(owner));
    }

    public void onClick(Window owner) {
        ComboBox<String> soundOptions = new ComboBox<>();
        soundOptions.getItems().addAll(SoundFactory.getAllSoundNames());
        VBox content = new VBox(soundOptions);
        Optional<ButtonType> result = DialogUI.showAlert(owner, Alert.AlertType.NONE, "Select New Sound", null, null, content, ButtonType.OK, ButtonType.CANCEL);
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String selectedSound = soundOptions.getValue();
            if (selectedSound != null) {
                this.command.setNewSoundName(selectedSound);
                this.command.execute(); // Execute the command with the selected sound
            }
        }
    }
}
