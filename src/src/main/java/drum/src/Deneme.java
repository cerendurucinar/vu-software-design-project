package drum.src;
import drum.src.drumsequencer.DrumSequence;
import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;
import drum.src.ui.ClearButton;
import drum.src.ui.PlayButton;
import drum.src.ui.RandomButton;
import drum.src.ui.SoundButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Deneme extends Application {
    public List<List<SoundButton>> soundButtonList = new ArrayList<>();


        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Deneme");

            try {
                DrumSequencer.synthesizer = MidiSystem.getSynthesizer();
                DrumSequencer.synthesizer.open();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
                Platform.exit();
                return;
            }

            GridPane gridPane = new GridPane(); // to show grid of JavaFX Buttons
            gridPane.setPadding(new Insets(15));
            gridPane.setHgap(5);
            gridPane.setVgap(5);
            gridPane.setAlignment(Pos.CENTER);

            int numRows = 3;
            int numCols = 6;

            List<String> curSeq = new ArrayList<>();
            for (int row = 0; row < numRows; row++) {
                List<SoundButton> rowList = new ArrayList<>();
                curSeq.add("-".repeat(numCols)); // initializing a clear sequence
                for (int col = 0; col < numCols; col++) {
                    rowList.add(null);
                }
                soundButtonList.add(rowList); // initializing the matrix of SoundButtons
            }

            DrumSequencer seq = new DrumSequencer(false, true, soundButtonList);
            DrumSequence sequence = new DrumSequence(curSeq, seq);
            PlayButton playButton = new PlayButton("Play", seq);
            ClearButton clearButton= new ClearButton("Clear", seq);
            RandomButton randomButton = new RandomButton("Random", sequence);
            HBox buttonBox = new HBox(playButton.getFxButton(), clearButton.getFxButton(), randomButton.getFxButton());

            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setSpacing(20);
            buttonBox.setPadding(new Insets(40));

            Iterator<List<SoundButton>> rowIterator = soundButtonList.iterator();

            int row = 0;
            while (rowIterator.hasNext() && row < numRows) {
                List<SoundButton> rowList = rowIterator.next();
                Iterator<SoundButton> colIterator = rowList.iterator();
                int col = 0;
                while (colIterator.hasNext() && col < numCols) { // check this
                    String btn_name = "Button " + (row * numCols + col + 1);
                    Sound sound = new Sound("Sound " + (row * numCols + col + 1), "Sound"+String.valueOf(row)+".mid");
                    SoundButton sbtn = new SoundButton(btn_name, sound, row, col);
                    soundButtonList.get(row).set(col, sbtn);
                    gridPane.add(sbtn.getBtn(), col, row);
                    col++;
                }
                row++;
            }

            VBox vbox = new VBox(buttonBox, gridPane);
            vbox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(vbox, 300, 200);
            primaryStage.setScene(scene);

            primaryStage.show();
        }

        public void stop(Synthesizer synthesizer) {
            if (DrumSequencer.synthesizer != null && DrumSequencer.synthesizer.isOpen()) {
                DrumSequencer.synthesizer.close();
            }
        }

        public static void main(String[] args) {
             launch(args);
        }

}





