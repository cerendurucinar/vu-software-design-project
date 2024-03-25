package drum.src.drumsequencer;

import drum.src.observer.Observer;
import drum.src.ui.SoundButton;

import java.util.List;
import java.util.Random;

public class DrumSequence implements Observer {
    private  List<String> currentSequence;
    private DrumSequencer drum_seq;

    public DrumSequence(List<String> currentSequence, DrumSequencer drum_sequencer) {
        this.currentSequence = currentSequence;
        this.drum_seq = drum_sequencer;
    }

    public void createRandomSequence(){
        List<List<SoundButton>> sbtnList = drum_seq.getSoundButtonList();
        Random rand = new Random();
        drum_seq.clearSequence();
        for (int r = 0; r < sbtnList.size(); r++){
            for (int c = 0; c < sbtnList.get(r).size(); c++){
                boolean randVal = rand.nextBoolean();
                if(randVal){
                    sbtnList.get(r).get(c).onClick();
                }
            }
        }
    }

    @Override
    public void update(int row, int col, boolean triggerState) {
        char charToReplace = '-';
        if(triggerState){
            charToReplace = '+';
        }
        String currentString = currentSequence.get(row);
        StringBuilder modifiedString = new StringBuilder(currentString);
        modifiedString.setCharAt(col, charToReplace);
        currentSequence.set(row, modifiedString.toString());
    }
}
