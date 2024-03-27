package drum.src.command;

import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;

public class ChangeVelocityCommand implements Command{
   // private DrumSequencer sequencer;
    DrumSequencer sequencer;
    private int row;
    private int newVelocity;
    public ChangeVelocityCommand(int row, int newVelocity) {
        this.sequencer = DrumSequencer.getInstance();
        this.row = row;
        this.newVelocity = newVelocity;
    }

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
