package drum.src;
import drum.src.drumsequencer.DrumSequence;
import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;
import drum.src.ui.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Deneme extends Application {
    public List<List<SoundButton>> soundButtonList = new ArrayList<>();
    public static final int NUM_ROWS = 4;
    public static final int NUM_COLS = 6;

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



            List<String> curSeq = new ArrayList<>();
            for (int row = 0; row < NUM_ROWS; row++) {
                List<SoundButton> rowList = new ArrayList<>();
                curSeq.add("-".repeat(NUM_COLS)); // initializing a clear sequence
                for (int col = 0; col < NUM_COLS; col++) {
                    rowList.add(null);
                }
                soundButtonList.add(rowList); // initializing the matrix of SoundButtons
            }

            DrumSequencer seq = DrumSequencer.getInstance();
            seq.setSoundButtonList(soundButtonList);
            DrumSequence sequence = new DrumSequence(curSeq, seq);
            PlayButton playButton = new PlayButton("Play", seq);
            ClearButton clearButton= new ClearButton("Clear", seq);
            RandomButton randomButton = new RandomButton("Random", sequence);
            Button velButton = new Button("Change Velocities");
            velButton.setOnAction( e-> showVelocityAdjustmentDialog(primaryStage));
            HBox buttonBox = new HBox(playButton.getFxButton(), clearButton.getFxButton(), randomButton.getFxButton(), velButton);

            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setSpacing(20);
            buttonBox.setPadding(new Insets(40));

            Iterator<List<SoundButton>> rowIterator = soundButtonList.iterator();

            int row = 0;
            while (rowIterator.hasNext() && row < NUM_ROWS) {
                List<SoundButton> rowList = rowIterator.next();
                Iterator<SoundButton> colIterator = rowList.iterator();
                int col = 0;
                while (colIterator.hasNext() && col < NUM_COLS) { // check this
                    String btn_name = "Button " + (row * NUM_COLS + col + 1);

                   // Sound sound = new Sound("Sound " + (row * numCols + col + 1), "Sound"+String.valueOf(row)+".mid");
                    String name = "Sound"+String.valueOf(row);
                    SoundButton sbtn = new SoundButton(btn_name, name , row, col);
                    sbtn.setObserver(sequence);

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
    public static void showVelocityAdjustmentDialog(Stage owner) { // TODO: CHANGE YOUR SEQUENCE AND CLASS DIAGRAM YOU DO NOT CHECK VELOCITY NOW

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Adjust Velocities");
        int numberOfRows = NUM_ROWS;
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox container = new VBox(10);
        for (int i = 0; i < numberOfRows; i++) {
            final int row = i;
            DrumSequencer seq = DrumSequencer.getInstance();
            Sound s = seq.getSoundButtonList().get(row).get(0).getSound();
            Slider velocitySlider = new Slider(0, 100, s.getVelocity()); // Min, Max, Initial Velocity
            velocitySlider.setShowTickLabels(true);
            velocitySlider.setShowTickMarks(true);

            velocitySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
                // Implement velocity update logic here
                s.changeVelocity(((Number) newValue).intValue());
            });
            container.getChildren().add(velocitySlider);
        }

        dialog.getDialogPane().setContent(container);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }



        public static void main(String[] args) {
             launch(args);
        }

}





