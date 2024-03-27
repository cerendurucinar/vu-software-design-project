package drum.src.ui;

import drum.src.Deneme;
import drum.src.command.ChangeDurationCommand;

import drum.src.command.Command;
import drum.src.drumsequencer.DrumSequencer;
import drum.src.sound.Sound;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ChangeDurationButton extends Button {
    private ChangeDurationCommand command;
    DrumSequencer sequencer;

    public ChangeDurationButton(String buttonText, ChangeDurationCommand command, Window owner) {

        super(buttonText);
        this.sequencer = DrumSequencer.getInstance();
        this.command = command;
        this.setOnAction(e -> onClick(owner));
    }

    public void onClick(Window owner) {
        VBox container = new VBox(10);
        Map<Integer, Integer> rowAndNewDuration = new HashMap<>();

        for (int i = 0; i < Deneme.NUM_ROWS; i++) {
            final int row = i;
            DrumSequencer seq = DrumSequencer.getInstance();
            Sound s = seq.getSoundButtonList().get(row).get(0).getSound();

            Slider durationSlider = new Slider(0, 100, s.getDuration());
            Label label = new Label(DrumSequencer.getInstance().getSoundButtonList().get(row).get(0).getSound().getSoundName());
            durationSlider.setShowTickLabels(true);
            durationSlider.setShowTickMarks(true);
            container.getChildren().add(label);
            durationSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
                rowAndNewDuration.put(row, newValue.intValue());
            });
            container.getChildren().add(durationSlider);


        }

        DialogUI.showAlert(owner, Alert.AlertType.CONFIRMATION, "Adjust Durations", "Adjust the duration for each sound:", null, container, ButtonType.OK, ButtonType.CANCEL)
            .ifPresent(response -> {
                if (response == ButtonType.OK) {
                  rowAndNewDuration.forEach((row, duration) -> {
                     command.setRow(row);
                     command.setNewDuration(duration);
                     command.execute();
                });
            }
        });





    }
}
