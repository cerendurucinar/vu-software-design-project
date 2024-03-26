package drum.src.ui;

import drum.src.drumsequencer.DrumSequence;
import drum.src.drumsequencer.DrumSequencer;
import drum.src.observer.Subject;
import drum.src.sound.Sound;
import drum.src.sound.SoundFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.List;

public class ChangeSoundButton extends Button {
    DrumSequencer sequencer = DrumSequencer.getInstance();
    private int row;
    private Button button;
    public ChangeSoundButton(String buttonText, DrumSequencer seq,int row, Window owner) {
        this.button = new Button(buttonText);
        this.button.setOnAction(e -> onClick(owner));
        this.row = row;
        this.sequencer = seq;

    }

    public void onClick(Window owner) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(owner);
        alert.setTitle("Select New Sound");

        ComboBox<String> soundOptions = new ComboBox<>();
        soundOptions.getItems().addAll(SoundFactory.getAllSoundNames());


        soundOptions.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                changeRowSound(row, newVal);
                //alert.close(); // Close the dialog after selection
            }
        });

        VBox content = new VBox(soundOptions);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();

    }
    public void changeRowSound(int row, String newSoundName) {
        // Example: Update the SoundButton's associated Sound in this row
        List<SoundButton> rowButtons = sequencer.getSoundButtonList().get(row);
        for (SoundButton button : rowButtons) {
            button.getBtn().setText(newSoundName);
            Sound newSound = SoundFactory.getSound(newSoundName);
            button.setSound(newSound);
        }
    }


    public Button getButton() {
        return button;
    }

}
