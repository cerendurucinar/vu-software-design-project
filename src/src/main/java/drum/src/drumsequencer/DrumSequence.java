package drum.src.drumsequencer;

import drum.src.observer.*;
import drum.src.ui.SoundButton;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *  this class represents a beat sequence. It implements Observer interface,
 *  and it observers changes in the SoundButton objects. The reason behind that is
 *  updating the currentSequence when a SoundButton is clicked. his class is responsible
 *  for generating random sequences, updating the sequence based on user interactions,
 *  and saving the sequence to a file.
 */

public class DrumSequence implements Observer {

    private  List<String> currentSequence;
    private DrumSequencer drum_seq;

    public DrumSequence(List<String> currentSequence) {
        this.currentSequence = currentSequence;
        this.drum_seq = DrumSequencer.getInstance();
    }

    public void createRandomSequence(){
        List<List<SoundButton>> sbtnList = drum_seq.getSoundButtonList();
        Random rand = new Random();
        drum_seq.clearSequence(); // first make sure that all SoundButtons are in notTriggered state
        Iterator<List<SoundButton>> rowIterator = sbtnList.iterator();
        while(rowIterator.hasNext()){
            Iterator<SoundButton> colIterator = rowIterator.next().iterator();
            while(colIterator.hasNext()){
                SoundButton button = colIterator.next();
                boolean randVal = rand.nextBoolean(); // getting a random bool value to randomly determine if a button is clicked or not
                if(randVal){
                    button.onClick();
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

    public void saveSequence(){
        String fileName = "src/src/main/resources/drum/src/data/data.txt"; // this is the path of the file that contains our preset or saved sequences
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (int i = 0; i< currentSequence.size(); i++){
                String row = currentSequence.get(i);
                writer.write(row);
            }

            writer.write("\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

}
