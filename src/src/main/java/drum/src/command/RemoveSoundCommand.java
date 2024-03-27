package drum.src.command;

import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;
import drum.src.sound.SoundFactory;

public class RemoveSoundCommand implements Command{
    // this class handles removing a specific sound from the app.
    private String soundName;

    public RemoveSoundCommand(String soundName) {
        this.soundName = soundName;
    }

    @Override
    public void execute() {
        if (soundName != null && !soundName.isEmpty()) {
            SoundFactory.removeSound(soundName);
        }
    }
    public RemoveSoundCommand() {

    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }
}


