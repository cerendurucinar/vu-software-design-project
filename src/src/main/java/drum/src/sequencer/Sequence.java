package drum.src.sequencer;

import drum.src.ui.SoundButton;

import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Random;

public class Sequence {
    private static List<String> currentSequence;
    private Sequencer sequencer;

    public Sequence(List<String> currentSequence, Sequencer sequencer) {
        this.currentSequence = currentSequence;
        this.sequencer = sequencer;
    }

    public void createRandomSequence(){
        List<List<SoundButton>> sbtnList = sequencer.getSoundButtonList();
        Random rand = new Random();
        sequencer.clearSequence();
        for (int r = 0; r < sbtnList.size(); r++){
            for (int c = 0; c < sbtnList.get(r).size(); c++){
                boolean randVal = rand.nextBoolean();
                if(randVal){
                    sbtnList.get(r).get(c).onClick();
                }
            }
        }
    }

    public static void updateSequence(int row, int col, boolean triggerState){
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
