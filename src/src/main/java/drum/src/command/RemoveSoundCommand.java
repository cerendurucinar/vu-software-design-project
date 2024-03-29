package drum.src.command;


import drum.src.sound.SoundFactory;

/**
 * This class handles removing a specific sound from the app.
 */
public class RemoveSoundCommand implements Command{

    private String soundName;


    @Override
    public void execute() {
        if (soundName != null && !soundName.isEmpty()) {
            SoundFactory.removeSound(soundName);
        }
    }
    public RemoveSoundCommand() {}

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }
}


