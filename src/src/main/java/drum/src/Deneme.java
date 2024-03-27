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

import java.io.*;
import java.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


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
            setupAddSoundButton(vbox,primaryStage);
            setupRemoveSoundButton(vbox,primaryStage);

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
        Button velButton = new Button("Change Velocities");
        Button durButton = new Button("Change Durations");
        Button selectSeqButton = new Button("Select Sequence");
        velButton.setOnAction( e-> showVelocityAdjustmentDialog(primaryStage));
        durButton.setOnAction(e -> showDurationAdjustmentDialog(primaryStage));

        selectSeqButton.setOnAction(e -> onSelectSequenceClick(primaryStage));

        ComboBox<DrumSequencer.TimeSignatureEnum> timesignaturecombobox = seq.createTimeSignature();

         buttonBox = new HBox(playButton.getFxButton(), clearButton.getFxButton(), randomButton.getFxButton(), velButton, durButton, timesignaturecombobox, saveButton.getFxButton(), selectSeqButton);


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
                // Create the command for changing sound in this row
                ChangeSoundCommand changeSoundCommand = new ChangeSoundCommand(seq, row);

                // Create the button that allows changing the sound, passing the command
                ChangeSoundButton changeSoundBtn = new ChangeSoundButton("Change Sound", changeSoundCommand, primaryStage);

                gridPane.add(changeSoundBtn, 0, row); // Directly add ChangeSoundButton to the grid

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


//    private void setupSoundButtons(GridPane gridPane, Stage primaryStage) {
//        for (int row = 0; row < NUM_ROWS; row++) {
//            List<SoundButton> rowList = new ArrayList<>();
//            ChangeSoundButton changeSoundBtn = new ChangeSoundButton("Change Sound",seq,row,primaryStage);
//            gridPane.add(changeSoundBtn.getButton(), 0, row);
//            for (int col = 0; col < NUM_COLS; col++) {
//                SoundButton sbtn = createSoundButton(row, col);
//                rowList.add(sbtn);
//                gridPane.add(sbtn.getBtn(), col + 1, row); // col+1 to account for the "+" button
//            }
//            soundButtonList.add(rowList);
//        }
//    }
    private SoundButton createSoundButton(int row, int col) {
        String name = "Sound" + row;
        return new SoundButton("Button " + (row * NUM_COLS + col + 1), name, row, col);
    }


    public void stop(Synthesizer synthesizer) {
            if (DrumSequencer.synthesizer != null && DrumSequencer.synthesizer.isOpen()) {
                DrumSequencer.synthesizer.close();
            }
        }
    public static void showVelocityAdjustmentDialog(Stage owner) { // TODO: CHANGE YOUR SEQUENCE AND CLASS DIAGRAM YOU DO NOT CHECK VELOCITY NOW

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Adjust Velocities");
        int numberOfRows = NUM_ROWS;
        dialog.initOwner(owner); // Set the owner to your primary stage
        dialog.initModality(Modality.APPLICATION_MODAL); // Make the d

        VBox container = new VBox(10); // 10 is the spacing between elements
        for (int i = 0; i < numberOfRows; i++) {
            final int row = i;
            DrumSequencer seq = DrumSequencer.getInstance();
            Sound s = seq.getSoundButtonList().get(row).get(0).getSound();
            Slider velocitySlider = new Slider(0, 100, s.getVelocity()); // Min, Max, Initial Velocity
            velocitySlider.setShowTickLabels(true);
            velocitySlider.setShowTickMarks(true);

            velocitySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
                // Implement velocity update logic here

                Command changeVelocity = new ChangeVelocityCommand(seq, row, ((Number) newValue).intValue());
                changeVelocity.execute();
            });
            container.getChildren().add(velocitySlider);
        }

        dialog.getDialogPane().setContent(container);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    public static void showDurationAdjustmentDialog(Stage owner) { // TODO: CHANGE YOUR SEQUENCE AND CLASS DIAGRAM YOU DO NOT CHECK VELOCITY NOW

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Adjust Durations");
        int numberOfRows = NUM_ROWS;
        dialog.initOwner(owner); // Set the owner to your primary stage
        dialog.initModality(Modality.APPLICATION_MODAL); // Make the d

        VBox container = new VBox(10); // 10 is the spacing between elements
        for (int i = 0; i < numberOfRows; i++) {
            final int row = i;
            DrumSequencer seq = DrumSequencer.getInstance();
            Sound s = seq.getSoundButtonList().get(row).get(0).getSound();
            Slider durationSlider = new Slider(0, 100, s.getDuration()); // Min, Max, Initial Velocity
            durationSlider.setShowTickLabels(true);
            durationSlider.setShowTickMarks(true);

            durationSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
                // Implement velocity update logic here
                Command changeDuration = new ChangeDurationCommand(seq, row, ((Number) newValue).intValue());
                changeDuration.execute();
            });
            container.getChildren().add(durationSlider);
        }

        dialog.getDialogPane().setContent(container);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }
    private void setupAddSoundButton(VBox vbox, Stage primaryStage) {
        Button addSoundBtn = new Button("+");
        addSoundBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select MIDI Sound File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDI Files", "*.mid", "*.midi"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                String soundName = selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.'));
                String soundFile = selectedFile.getAbsolutePath();
                SoundFactory.addSound(soundName, soundFile);
            }
        });
        vbox.getChildren().add(addSoundBtn);
    }

    public void onSelectSequenceClick(Window owner) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(owner);
        alert.setTitle("Select Sequence");

        ComboBox<String> seqOptions = new ComboBox<>();
        Set<String> sequenceSet = new HashSet<String>();
        String filePath = "src/src/main/resources/drum/src/data/data.txt"; // Specify the path to your text file

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

    private void setupRemoveSoundButton(VBox vbox, Stage primaryStage) {
        Button removeSoundBtn = new Button("-");
        removeSoundBtn.setOnAction(event -> {
            if (seq.isOn()) {
                // Inform the user that removal is not allowed during playback
                Alert playbackAlert = new Alert(Alert.AlertType.WARNING);
                playbackAlert.initModality(Modality.APPLICATION_MODAL);
                playbackAlert.initOwner(primaryStage);
                playbackAlert.setTitle("Removal Not Allowed");
                playbackAlert.setHeaderText("Cannot Remove Sound During Playback");
                playbackAlert.setContentText("Please stop the sequence before removing a sound.");
                playbackAlert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initOwner(primaryStage);
                alert.setTitle("Remove A Sound");
                alert.setHeaderText("Select a Sound");

                ComboBox<String> soundOptions = new ComboBox<>();
                soundOptions.getItems().addAll(SoundFactory.getAllSoundNames());

                VBox content = new VBox(soundOptions);
                alert.getDialogPane().setContent(content);
                ButtonType REMOVE_SOUND = new ButtonType("Remove Sound");
                alert.getButtonTypes().setAll(REMOVE_SOUND, ButtonType.CANCEL);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == REMOVE_SOUND) {
                    // Show confirmation dialog for removing sound
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.initModality(Modality.APPLICATION_MODAL);
                    confirmationDialog.initOwner(primaryStage);
                    // confirmationDialog.setTitle("Confirmation Dialog");
                    confirmationDialog.setHeaderText("Remove Sound");
                    confirmationDialog.setContentText("Are you sure you want to remove this sound?");

                    Optional<ButtonType> confirmationResult = confirmationDialog.showAndWait();
                    if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {
                        String selectedSound = soundOptions.getValue();

                        // Create and execute the remove sound command
                        RemoveSoundCommand removeSoundCommand = new RemoveSoundCommand(selectedSound);
                        removeSoundCommand.execute();
                        // Close the dialogs after removing the sound
                        confirmationDialog.close();
                        alert.close();
                    }
                }
            }
        });





        vbox.getChildren().add(removeSoundBtn);
    }



    public static void main(String[] args) {
            launch(args);
        }

}





