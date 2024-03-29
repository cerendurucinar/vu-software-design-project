package drum.src.command;


import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class AddSoundButton extends Button {

    private AddSoundCommand command;
    public AddSoundButton(String buttonText, AddSoundCommand command, Window owner) {
        super(buttonText);
        this.command = command;
        this.setOnAction(e -> onClick(owner));
    }

    private void onClick(Window owner) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select MIDI Sound File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDI Files", "*.mid", "*.midi"));
        File selectedFile = fileChooser.showOpenDialog(owner);
        if (selectedFile != null) {
            this.command.setSelectedFile(selectedFile);
            this.command.execute();
        }
    }
}