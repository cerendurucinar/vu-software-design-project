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

//public class ChangeSoundButton extends Button {
//    DrumSequencer sequencer = DrumSequencer.getInstance();
//    private int row;
//    private Button button;
//    private Command command;
//    public ChangeSoundButton(String buttonText, Command command, DrumSequencer seq, int row, Window owner) {
//        this.button = new Button(buttonText);
//        this.button.setOnAction(e -> onClick(owner));
//        this.row = row;
//        this.sequencer = seq;
//        this.setOnAction(e -> this.command.execute());
//
//    }
//
//    public void onClick(Window owner) {
//
//        Alert alert = new Alert(Alert.AlertType.NONE);
//        alert.initModality(Modality.APPLICATION_MODAL);
//        alert.initOwner(owner);
//        alert.setTitle("Select New Sound");
//
//        ComboBox<String> soundOptions = new ComboBox<>();
//        soundOptions.getItems().addAll(SoundFactory.getAllSoundNames());
//
//        VBox content = new VBox(soundOptions);
//        alert.getDialogPane().setContent(content);
//        alert.getDialogPane().getButtonTypes().addAll( ButtonType.OK, ButtonType.CANCEL);
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            String selectedSound = soundOptions.getValue();
//            if (selectedSound != null) {
//                // Logic to change the sound for this row
//                changeRowSound(row, selectedSound);
//
//            }
//        }
//    }
//
//
//    public void changeRowSound(int row, String newSoundName) {
//        // Example: Update the SoundButton's associated Sound in this row
//        List<SoundButton> rowButtons = sequencer.getSoundButtonList().get(row);
//        for (SoundButton button : rowButtons) {
//            button.getBtn().setText(newSoundName);
//            Sound newSound = SoundFactory.getSound(newSoundName);
//            button.setSound(newSound);
//        }
//    }
//
//
//    public Button getButton() {
//        return button;
//    }
//
//}
public class ChangeSoundButton extends Button {
    private ChangeSoundCommand command;

    public ChangeSoundButton(String buttonText, ChangeSoundCommand command, Window owner) {
        super(buttonText); // Use "super" to set the button text, leveraging inheritance
        this.command = command;
        this.setOnAction(e -> onClick(owner));
    }

    public void onClick(Window owner) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(owner);
        alert.setTitle("Select New Sound");

        ComboBox<String> soundOptions = new ComboBox<>();
        soundOptions.getItems().addAll(SoundFactory.getAllSoundNames());

        VBox content = new VBox(soundOptions);
        alert.getDialogPane().setContent(content);
        alert.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String selectedSound = soundOptions.getValue();
            if (selectedSound != null) {
                this.command.setNewSoundName(selectedSound);
                this.command.execute(); // Execute the command with the selected sound
            }
        }
    }
}
