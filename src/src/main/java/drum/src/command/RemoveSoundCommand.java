package drum.src.command;

import drum.src.sound.Sound;
import drum.src.sound.SoundFactory;

public class RemoveSoundCommand implements Command{
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

}


