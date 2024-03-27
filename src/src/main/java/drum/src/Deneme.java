package drum.src;
import drum.src.command.*;
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
import javafx.scene.control.Dialog;
import javafx.scene.control.Slider;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import javafx.stage.Stage;
import javafx.stage.Window;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Deneme extends Application {
    public List<List<SoundButton>> soundButtonList = new ArrayList<>();

    public static final int NUM_ROWS = 4;
    public static final int NUM_COLS = 6;
    DrumSequencer seq = DrumSequencer.getInstance();
    DrumSequence sequence;
    List<String> curSeq = new ArrayList<>();
    HBox buttonBox;



    @Override

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Deneme");

        initializeSynthesizer();
        GridPane gridPane = setupGridPane(primaryStage);
        buttonBox = setupControlButtons(primaryStage);
        VBox vbox = setupUI(primaryStage, gridPane);
        AddSoundButton addSoundBtn = new AddSoundButton("Add New Sound", new AddSoundCommand(),primaryStage);
        vbox.getChildren().add(addSoundBtn);

        RemoveSoundButton removeSoundBtn = new RemoveSoundButton("Delete A Sound",new RemoveSoundCommand(),primaryStage );
        vbox.getChildren().add(removeSoundBtn);

        vbox.setAlignment(Pos.CENTER);

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


    private VBox setupUI(Stage primaryStage, GridPane gridPane) {

        VBox vbox = new VBox(10, buttonBox, gridPane);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private HBox setupControlButtons(Stage primaryStage) {


        PlayButton playButton = new PlayButton("Play", seq);
        ClearButton clearButton= new ClearButton("Clear", seq);
        RandomButton randomButton = new RandomButton("Random", sequence);
        SaveSequenceButton saveButton = new SaveSequenceButton("Save Sequence", sequence);
        Button selectSeqButton = new Button("Select Sequence");
        selectSeqButton.setOnAction(e->{onSelectSequenceClick(primaryStage);});
        ChangeVelocityButton changeVelocityButton = new ChangeVelocityButton("Change Velocities", new ChangeVelocityCommand(),primaryStage);
        ChangeDurationButton changeDurationButton = new ChangeDurationButton("Change Durations", new ChangeDurationCommand(),primaryStage);
        ComboBox<DrumSequencer.TimeSignatureEnum> timesignaturecombobox = seq.createTimeSignature();

        buttonBox = new HBox(playButton.getFxButton(), clearButton.getFxButton(), randomButton.getFxButton(), changeVelocityButton, changeDurationButton, timesignaturecombobox, saveButton.getFxButton(), selectSeqButton);
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
        sequence = new DrumSequence(curSeq);

        Iterator<List<SoundButton>> rowIterator = soundButtonList.iterator();

        int row = 0;
        while (rowIterator.hasNext()) {
            List<SoundButton> rowList = rowIterator.next();
            Iterator<SoundButton> colIterator = rowList.iterator();
            ChangeSoundCommand changeSoundCommand = new ChangeSoundCommand(seq, row);
            ChangeSoundButton changeSoundBtn = new ChangeSoundButton("Change Sound", changeSoundCommand, primaryStage);
            gridPane.add(changeSoundBtn, 0, row);
            int col = 0;
            while (colIterator.hasNext()) {
                String btn_name = "Button" + (row * NUM_COLS + col + 1);
                String name = "Sound"+String.valueOf(row);
                SoundButton sbtnIt = colIterator.next();
                SoundButton sbtn = new SoundButton(btn_name, name , row, col);
                sbtnIt = sbtn;
                sbtn.setObserver(sequence);

                soundButtonList.get(row).set(col, sbtn);
                gridPane.add(sbtn.getBtn(), col+1, row);
                col++;
            }
            row++;
        }
        return gridPane;

    }
    public void onSelectSequenceClick(Window owner) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(owner);
        alert.setTitle("Select Sequence");

        ComboBox<String> seqOptions = new ComboBox<>();
        Set<String> sequenceSet = new HashSet<String>();
        String filePath = "src/src/main/resources/drum/src/data/data.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int counter = 1;
            while ((line = reader.readLine()) != null) {
                if(line == "\n"){
                    continue;
                }
                sequenceSet.add("Sequence " + String.valueOf(counter));
                counter++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        seqOptions.getItems().addAll(sequenceSet);


        seqOptions.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                DrumSequencer seq = DrumSequencer.getInstance();
                List<List<SoundButton>> sbtnList = seq.getSoundButtonList();
                int selectedVal = Integer.parseInt(seqOptions.getValue().substring(9));

                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    int counter = 1;
                    while ((line = reader.readLine()) != null) {
                        if (counter == selectedVal) {
                            for (int row = 0; row < sbtnList.size(); row++){
                                for (int col = 0; col < sbtnList.get(row).size(); col++){
                                    SoundButton sb = sbtnList.get(row).get(col);
                                    int idx = 6*row+col;
                                    if((line.charAt(idx) == '+' && !sb.getIsTriggered()) || (line.charAt(idx) == '-' && sb.getIsTriggered())) {
                                        sb.onClick();
                                    }
                                }
                            }
                            break;
                        }
                        counter++;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        VBox content = new VBox(seqOptions);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();

    }
    public static void main(String[] args) {
        launch(args);
    }

}




