package drum.src.command;

import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;

/**
 * This class handles changing the velocity of a specific row's sound
 */
public class ChangeVelocityCommand implements Command{

    DrumSequencer sequencer;
    private int row;
    private int newVelocity;

    public ChangeVelocityCommand() {
        this.sequencer = DrumSequencer.getInstance();

    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setNewVelocity(int newVelocity) {
        this.newVelocity = newVelocity;
    }

    @Override
    public void execute() {
        Sound sound = sequencer.getSoundButtonList().get(row).get(0).getSound();
        sound.changeVelocity((int) newVelocity);
    }
}
