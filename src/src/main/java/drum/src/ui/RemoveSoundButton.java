package drum.src.ui;

import drum.src.command.ChangeDurationCommand;
import drum.src.command.RemoveSoundCommand;
import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.SoundFactory;
import drum.src.ui.DialogUI;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

public class RemoveSoundButton extends Button {
    private final RemoveSoundCommand command;
    DrumSequencer sequencer;

    public RemoveSoundButton(String buttonText, RemoveSoundCommand command, Window owner) {
        super(buttonText);
        this.sequencer = DrumSequencer.getInstance();
        this.command = command;
        this.setOnAction(e -> onClick(owner));

    }

    private void onClick(Window owner) {
         sequencer = DrumSequencer.getInstance();
        if (sequencer.isOn()) {
            DialogUI.showAlert(owner, Alert.AlertType.WARNING,"Removal Not Allowed","Cannot Remove Sound During Playback","Please stop the sequence before removing a sound.", null,ButtonType.OK);

        } else {
            ButtonType REMOVE_SOUND = new ButtonType("Remove Sound");
            ComboBox<String> soundOptions = new ComboBox<>();
            soundOptions.getItems().addAll(SoundFactory.getAllSoundNames());
            Optional<ButtonType> result = DialogUI.showAlert(owner, Alert.AlertType.CONFIRMATION, "Remove A Sound", "Select a Sound", null, new VBox(soundOptions), REMOVE_SOUND, ButtonType.CANCEL);


            if (result.isPresent() && result.get() == REMOVE_SOUND) {
                Optional<ButtonType> confirmationResult = DialogUI.showAlert(owner, Alert.AlertType.CONFIRMATION, "", "Are you sure you want to remove this sound?", "", null, ButtonType.OK, ButtonType.CANCEL);

                if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {
                    String selectedSound = soundOptions.getValue();
                    if (selectedSound != null) {
                        this.command.setSoundName(selectedSound);
                        this.command.execute(); // Execute the command with the selected sound
                    }


                }
            }
        }
    }
}