package drum.src;
import drum.src.drumsequencer.DrumSequence;
import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;
import drum.src.sound.SoundFactory;
import drum.src.ui.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Deneme extends Application {
    public List<List<SoundButton>> soundButtonList = new ArrayList<>();
    private static final int NUM_ROWS = 3;
    private static final int NUM_COLS = 6;
    DrumSequencer seq = DrumSequencer.getInstance();
    DrumSequence sequence;
    List<String> curSeq = new ArrayList<>();
    HBox buttonBox;
    @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Deneme");

            initializeSynthesizer();
            buttonBox = setupControlButtons(primaryStage);
            GridPane gridPane = setupGridPane(primaryStage);
            VBox vbox = setupUI(primaryStage, gridPane);
            setupAddSoundButton(vbox,primaryStage);

//            List<String> curSeq = new ArrayList<>();
//            for (int row = 0; row < numRows; row++) {
//                List<SoundButton> rowList = new ArrayList<>();
//                curSeq.add("-".repeat(numCols)); // initializing a clear sequence
//                for (int col = 0; col < numCols; col++) {
//                    rowList.add(null);
//                }
//                soundButtonList.add(rowList); // initializing the matrix of SoundButtons
//            }
//
//            DrumSequencer seq = DrumSequencer.getInstance();
//            seq.setSoundButtonList(soundButtonList);
//            DrumSequence sequence = new DrumSequence(curSeq, seq);
//            PlayButton playButton = new PlayButton("Play", seq);
//            ClearButton clearButton= new ClearButton("Clear", seq);
//            RandomButton randomButton = new RandomButton("Random", sequence);
//            HBox buttonBox = new HBox(playButton.getFxButton(), clearButton.getFxButton(), randomButton.getFxButton());
//
//            buttonBox.setAlignment(Pos.CENTER);
//            buttonBox.setSpacing(20);
//            buttonBox.setPadding(new Insets(40));


//            Iterator<List<SoundButton>> rowIterator = soundButtonList.iterator();
//
//            int row = 0;
//            while (rowIterator.hasNext() && row < numRows) {
//                List<SoundButton> rowList = rowIterator.next();
//                Iterator<SoundButton> colIterator = rowList.iterator();
//                int col = 0;
//                while (colIterator.hasNext() && col < numCols) { // check this
//                    String btn_name = "Button " + (row * numCols + col + 1);
//
//                   // Sound sound = new Sound("Sound " + (row * numCols + col + 1), "Sound"+String.valueOf(row)+".mid");
//                    String name = "Sound"+String.valueOf(row);
//                    SoundButton sbtn = new SoundButton(btn_name, name , row, col);
//                    sbtn.setObserver(sequence);
//
//                    soundButtonList.get(row).set(col, sbtn);
//                    gridPane.add(sbtn.getBtn(), col, row);
//                    col++;
//                }
//                row++;
//            }

   //         VBox vbox = new VBox(buttonBox, gridPane);
     //       vbox.setAlignment(Pos.CENTER);


            Scene scene = new Scene(vbox, 300, 200);
            primaryStage.setScene(scene);

            primaryStage.show();
        }
    private void initializeSynthesizer() {
        try {
            DrumSequencer.synthesizer = MidiSystem.getSynthesizer();
            DrumSequencer.synthesizer.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    private void setupAddSoundButton(VBox vbox, Stage primaryStage) {
        Button addSoundBtn = new Button("+");
        addSoundBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select MIDI Sound File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDI Files", "*.mid", "*.midi"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                String soundName = selectedFile.getName();
                String soundFile = selectedFile.getAbsolutePath();
                SoundFactory.addSound(soundName, soundFile);
            }
        });
        vbox.getChildren().add(addSoundBtn);
    }

    private void setupChangeSoundButtons(GridPane gridPane, ChangeSoundButton changeSoundButton) {
        for (int row = 0; row < NUM_ROWS; row++) {
            ComboBox<String> soundSelector = new ComboBox<>();
            soundSelector.getItems().addAll(SoundFactory.getAllSoundNames());

            int finalRow = row;
            soundSelector.valueProperty().addListener((obs, oldVal, newSoundName) -> {
                // Logic to change the sound for this row
                changeSoundButton.changeRowSound(finalRow, newSoundName);
            });
            gridPane.add(changeSoundButton.getButton(), 0, row);
            //gridPane.add(new Label("Change Sound:"), 0, row);
            gridPane.add(soundSelector, 1, row); // Adjust column index as needed
        }
    }

    private VBox setupUI(Stage primaryStage, GridPane gridPane) {

        VBox vbox = new VBox(10, buttonBox, gridPane);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private HBox setupControlButtons(Stage primaryStage) {


        PlayButton playButton = new PlayButton("Play", seq);
        ClearButton clearButton= new ClearButton("Clear", seq);
        RandomButton randomButton = new RandomButton("Random", sequence);

        buttonBox = new HBox(playButton.getFxButton(), clearButton.getFxButton(), randomButton.getFxButton());

        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);
        buttonBox.setPadding(new Insets(40));
        return buttonBox;


    }



    private GridPane setupGridPane(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER);
        for (int row = 0; row < NUM_ROWS; row++) {
            List<SoundButton> rowList = new ArrayList<>();
            curSeq.add("-".repeat(NUM_COLS)); // initializing a clear sequence
            for (int col = 0; col < NUM_COLS; col++) {
                rowList.add(null);
            }
            soundButtonList.add(rowList); // initializing the matrix of SoundButtons
        }

        seq.setSoundButtonList(soundButtonList);
        sequence = new DrumSequence(curSeq, seq);

        Iterator<List<SoundButton>> rowIterator = soundButtonList.iterator();

            int row = 0;
            while (rowIterator.hasNext() && row < NUM_ROWS) {
                List<SoundButton> rowList = rowIterator.next();
                Iterator<SoundButton> colIterator = rowList.iterator();
                ChangeSoundButton changeSoundBtn = new ChangeSoundButton("Change Sound",seq,row,primaryStage);
                //buttonBox.getChildren().add(changeSoundBtn);
                gridPane.add(changeSoundBtn.getButton(), 0, row);
                int col = 0;
                while (colIterator.hasNext() && col < NUM_COLS) { // check this
                    String btn_name = "Button" + (row * NUM_COLS + col + 1);

                   // Sound sound = new Sound("Sound " + (row * numCols + col + 1), "Sound"+String.valueOf(row)+".mid");
                    String name = "Sound"+String.valueOf(row);
                    SoundButton sbtn = new SoundButton(btn_name, name , row, col);
                    sbtn.setObserver(sequence);

                    soundButtonList.get(row).set(col, sbtn);
                    gridPane.add(sbtn.getBtn(), col+1, row);
                    col++;
                }
                row++;
            }


        return gridPane;

    }


    private void setupSoundButtons(GridPane gridPane, Stage primaryStage) {
        for (int row = 0; row < NUM_ROWS; row++) {
            List<SoundButton> rowList = new ArrayList<>();
            ChangeSoundButton changeSoundBtn = new ChangeSoundButton("Change Sound",seq,row,primaryStage);
            gridPane.add(changeSoundBtn.getButton(), 0, row);
            for (int col = 0; col < NUM_COLS; col++) {
                SoundButton sbtn = createSoundButton(row, col);
                rowList.add(sbtn);
                gridPane.add(sbtn.getBtn(), col + 1, row); // col+1 to account for the "+" button
            }
            soundButtonList.add(rowList);
        }
    }
    private SoundButton createSoundButton(int row, int col) {
        String name = "Sound" + row;
        return new SoundButton("Button " + (row * NUM_COLS + col + 1), name, row, col);
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





