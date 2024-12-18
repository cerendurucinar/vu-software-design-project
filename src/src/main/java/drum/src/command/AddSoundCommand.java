package drum.src.command;

import drum.src.sound.SoundFactory;
import java.io.File;

/**
 *  This class handles adding a sound to the app.
 */
public class AddSoundCommand implements Command {

    private File selectedFile;

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
