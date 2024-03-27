package drum.src.command;

import drum.src.sound.SoundFactory;
import java.io.File;

public class AddSoundCommand implements Command {
    // this class handles adding a sound to the app.
    private File selectedFile;

    public AddSoundCommand(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public AddSoundCommand() {
    }

    @Override
    public void execute() {
        if (selectedFile != null) {
            String soundName = selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.'));
            String soundFile = selectedFile.getAbsolutePath();
            SoundFactory.addSound(soundName, soundFile);
        }
    }
}
