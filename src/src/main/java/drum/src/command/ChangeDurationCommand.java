package drum.src.command;

import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;

/**
 * This class handles changing the duration of a specific row's sound
 */
public class ChangeDurationCommand implements Command{
    // this class handles changing the duration of a specific row's Sound.
    private DrumSequencer sequencer;
    private int row;
    private int newDuration;

    public ChangeDurationCommand() {
        this.sequencer = DrumSequencer.getInstance();
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setNewDuration(int newDuration) {
        this.newDuration = newDuration;
    }

    @Override
    public void execute() {
        Sound sound = sequencer.getSoundButtonList().get(row).get(0).getSound();
        sound.changeDuration(newDuration);
    }
}
