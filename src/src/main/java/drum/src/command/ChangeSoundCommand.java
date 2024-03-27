package drum.src.command;

import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;
import drum.src.sound.SoundFactory;
import drum.src.ui.SoundButton;

import java.util.List;

//public class ChangeSoundCommand implements Command{
//    private SoundFactory soundFactory;
//    private int row;
//    private String newSoundName;
//
//    public ChangeSoundCommand(SoundFactory soundFactory, int row, String newSoundName) {
//        this.soundFactory = soundFactory;
//        this.row = row;
//        this.newSoundName = newSoundName;
//    }
//
//    public void setSoundFactory(SoundFactory soundFactory) {
//        this.soundFactory = soundFactory;
//    }
//
//    public void setRow(int row) {
//        this.row = row;
//    }
//
//    public void setNewSoundName(String newSoundName) {
//        this.newSoundName = newSoundName;
//    }
//
//    @Override
//    public void execute() {
//        soundFactory.changeRowSound(row,newSoundName);
//    }
//}
public class ChangeSoundCommand implements Command {
    private DrumSequencer sequencer;
    private int row;
    private String newSoundName; // This will be set later

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
            // Place your logic to change the sound here
            List<SoundButton> rowButtons = sequencer.getSoundButtonList().get(row);
            for (SoundButton button : rowButtons) {
                button.getBtn().setText(newSoundName);
                Sound newSound = SoundFactory.getSound(newSoundName);
                button.setSound(newSound);
            }
        }
    }
}
