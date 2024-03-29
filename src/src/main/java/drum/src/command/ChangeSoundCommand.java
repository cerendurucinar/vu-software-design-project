package drum.src.command;

import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;
import drum.src.sound.SoundFactory;
import drum.src.ui.SoundButton;

import java.util.List;

/**
 * This class handles changing the sound of all buttons in a specified row
 */
public class ChangeSoundCommand implements Command {
    private DrumSequencer sequencer;
    private int row;
    private String newSoundName;

    public ChangeSoundCommand(DrumSequencer sequencer, int row) {
        this.sequencer = sequencer;
        this.row = row;
    }

    public void setNewSoundName(String newSoundName) {
        this.newSoundName = newSoundName;
    }

    @Override
    public void execute() {
        if (newSoundName != null) {
            List<SoundButton> rowButtons = sequencer.getSoundButtonList().get(row);
            for (SoundButton button : rowButtons) {
                button.getBtn().setText(newSoundName);
                Sound newSound = SoundFactory.getSound(newSoundName);
                button.setSound(newSound);
            }
        }
    }
}
