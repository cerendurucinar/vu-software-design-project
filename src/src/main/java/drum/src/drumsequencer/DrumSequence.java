package drum.src.drumsequencer;

import drum.src.observer.*;
import drum.src.ui.SoundButton;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

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

    public void saveSequence(){
        String fileName = "src/src/main/resources/drum/src/data/data.txt"; // Name of the text file to write to

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Write data to the file
            for (int i = 0; i < currentSequence.size(); i++){
                String row = currentSequence.get(i);
                writer.write(row);
            }
            writer.write("\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

}
