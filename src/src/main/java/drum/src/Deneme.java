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

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

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
        //Button velButton = new Button("Change Velocities");
        // ChangeVelocityCommand velocityCommand = new ChangeVelocityCommand(seq);

        ChangeVelocityButton changeVelocityButton = new ChangeVelocityButton("Change Velocities", new ChangeVelocityCommand(),primaryStage);
        ChangeDurationButton changeDurationButton = new ChangeDurationButton("Change Durations", new ChangeDurationCommand(),primaryStage);

        //Button durButton = new Button("Change Durations");
        //velButton.setOnAction( e-> showVelocityAdjustmentDialog(primaryStage));
        //durButton.setOnAction(e -> showDurationAdjustmentDialog(primaryStage));
        ComboBox<DrumSequencer.TimeSignatureEnum> timesignaturecombobox = seq.createTimeSignature();

        buttonBox = new HBox(playButton.getFxButton(), clearButton.getFxButton(), randomButton.getFxButton(), changeVelocityButton, changeDurationButton, timesignaturecombobox, saveButton.getFxButton());


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
//    public static void showVelocityAdjustmentDialog(Stage owner) { // TODO: CHANGE YOUR SEQUENCE AND CLASS DIAGRAM YOU DO NOT CHECK VELOCITY NOW
//
//
//        Map<Integer, Integer> rowAndNewVelocity = new HashMap<>();
//        VBox container = new VBox(10); // 10 is the spacing between elements
//
//        for (int i = 0; i < NUM_ROWS; i++) {
//            final int row = i;
//            DrumSequencer seq = DrumSequencer.getInstance();
//            Sound s = seq.getSoundButtonList().get(row).get(0).getSound();
//            Slider velocitySlider = new Slider(0, 100, s.getVelocity()); // Min, Max, Initial Velocity
//            velocitySlider.setShowTickLabels(true);
//            velocitySlider.setShowTickMarks(true);
//
//
//            velocitySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
//                rowAndNewVelocity.put(row, ((Number) newValue).intValue());
//            });
//            container.getChildren().add(velocitySlider);
//        }
//
//        Optional<ButtonType> result = DialogUI.showAlert(owner, Alert.AlertType.CONFIRMATION, "Adjust Velocities", "Adjust the velocity for each sound:", null, container,ButtonType.OK, ButtonType.CANCEL);
//        result.ifPresent(response -> {
//            if (response == ButtonType.OK) {
//                rowAndNewVelocity.forEach((row, velocity) -> {
//                    Command changeVelocity = new ChangeVelocityCommand( row, velocity);
//                    changeVelocity.execute();
//                });
//            }
//        });
//
//
//    }

    //    public static void showDurationAdjustmentDialog(Stage owner) { // TODO: CHANGE YOUR SEQUENCE AND CLASS DIAGRAM YOU DO NOT CHECK VELOCITY NOW
//
//        VBox container = new VBox(10); // 10 is the spacing between elements
//        Map<Integer, Integer> rowAndNewDuration = new HashMap<>();
//        for (int i = 0; i < NUM_ROWS; i++) {
//            final int row = i;
//            DrumSequencer seq = DrumSequencer.getInstance();
//            Sound s = seq.getSoundButtonList().get(row).get(0).getSound();
//            Slider durationSlider = new Slider(0, 100, s.getDuration()); // Min, Max, Initial Velocity
//            durationSlider.setShowTickLabels(true);
//            durationSlider.setShowTickMarks(true);
//
//
//            durationSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
//                rowAndNewDuration.put(row, newValue.intValue());
//            });
//            container.getChildren().add(durationSlider);
//        }
//        Optional<ButtonType> result = DialogUI.showAlert(owner, Alert.AlertType.CONFIRMATION, "Adjust Durations", "Adjust the duration for each sound:", null, container, ButtonType.OK, ButtonType.CANCEL);
//        result.ifPresent(response -> {
//            if (response == ButtonType.OK) {
//                rowAndNewDuration.forEach((row, duration) -> {
//                    Command changeDuration = new ChangeDurationCommand(DrumSequencer.getInstance(), row, duration);
//                    changeDuration.execute();
//                });
//            }
//        });
//
//    }
//    private void setupAddSoundButton(VBox vbox, Stage primaryStage) {
//        Button addSoundBtn = new Button("+");
//        addSoundBtn.setOnAction(e -> {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Select MIDI Sound File");
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDI Files", "*.mid", "*.midi"));
//            File selectedFile = fileChooser.showOpenDialog(primaryStage);
//            if (selectedFile != null) {
//                String soundName = selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.'));
//                String soundFile = selectedFile.getAbsolutePath();
//                SoundFactory.addSound(soundName, soundFile);
//            }
//        });
//        vbox.getChildren().add(addSoundBtn);
//    }

//    private void setupRemoveSoundButton(VBox vbox, Stage primaryStage) {
//        Button removeSoundBtn = new Button("-");
//        removeSoundBtn.setOnAction(event -> {
//            if (seq.isOn()) {
//                // Inform the user that removal is not allowed during playback
//                DialogUI.showAlert(primaryStage, Alert.AlertType.WARNING,"Removal Not Allowed","Cannot Remove Sound During Playback","Please stop the sequence before removing a sound.", null,ButtonType.OK);
//
//            }else {
//                ButtonType REMOVE_SOUND = new ButtonType("Remove Sound");
//                ComboBox<String> soundOptions = new ComboBox<>();
//                soundOptions.getItems().addAll(SoundFactory.getAllSoundNames());
//                Optional<ButtonType> result = DialogUI.showAlert(primaryStage, Alert.AlertType.CONFIRMATION, "Remove A Sound", "Select a Sound", null, new VBox(soundOptions), REMOVE_SOUND, ButtonType.CANCEL);
//
//
//                if (result.isPresent() && result.get() == REMOVE_SOUND) {
//                    Optional<ButtonType> confirmationResult = DialogUI.showAlert(primaryStage, Alert.AlertType.CONFIRMATION, "", "Are you sure you want to remove this sound?", "", null, ButtonType.OK, ButtonType.CANCEL);
//
//                    if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {
//                        String selectedSound = soundOptions.getValue();
//
//                        // Create and execute the remove sound command
//                        RemoveSoundCommand removeSoundCommand = new RemoveSoundCommand(selectedSound);
//                        removeSoundCommand.execute();
//
//                    }
//                }
//            }
//        });
//
//        vbox.getChildren().add(removeSoundBtn);
//    }



    public static void main(String[] args) {
        launch(args);
    }

}




