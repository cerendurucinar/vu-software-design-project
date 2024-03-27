package drum.src.command;

import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;

public class ChangeVelocityCommand implements Command{
    private DrumSequencer sequencer;
    private int row;
    private int newVelocity;
    public ChangeVelocityCommand(DrumSequencer sequencer, int row, int newVelocity) {
        this.sequencer = sequencer;
        this.row = row;
        this.newVelocity = newVelocity;
    }
    @Override
    public void execute() {
        Sound sound = sequencer.getSoundButtonList().get(row).get(0).getSound();
        sound.changeVelocity((int) newVelocity);
    }
}
