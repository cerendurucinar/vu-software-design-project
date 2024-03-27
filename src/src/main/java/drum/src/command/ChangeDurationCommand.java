package drum.src.command;

import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;

public class ChangeDurationCommand implements Command{
    private DrumSequencer sequencer;
    private int row;
    private int newDuration;

    public ChangeDurationCommand(DrumSequencer sequencer, int row, int newDuration) {
        this.sequencer = sequencer;
        this.row = row;
        this.newDuration = newDuration;
    }
    @Override
    public void execute() {
        Sound sound = sequencer.getSoundButtonList().get(row).get(0).getSound();
        sound.changeDuration(newDuration);
    }
}
