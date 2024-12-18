package drum.src.command;

import drum.src.DrumMachineMain;
import drum.src.drumsequencer.DrumSequencer;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.HashMap;
import java.util.Map;

/**
 * It is a UI component that presents the user with sliders for adjusting the velocity (volume)
 * of each sound in a drum sequencer. It extends the Button class and uses the command pattern
 * for executing the velocity adjustments.
 */


public class ChangeVelocityButton extends Button {
    private ChangeVelocityCommand command;
    DrumSequencer sequencer;


    public ChangeVelocityButton(String buttonText, ChangeVelocityCommand command, Window owner) {

        super(buttonText);
        this.sequencer = DrumSequencer.getInstance();
        this.command = command;
        this.setOnAction(e -> onClick(owner));
    }

    public void onClick(Window owner) {
        VBox container = new VBox(10);
        Map<Integer, Integer> rowAndNewVelocity = new HashMap<>();

        for (int i = 0; i < DrumMachineMain.NUM_ROWS; i++) {
            final int row = i;
            int initialVelocity = DrumSequencer.getInstance().getSoundButtonList().get(row).get(0).getSound().getVelocity();

            Slider velocitySlider = new Slider(0, 100, initialVelocity);
            Label label = new Label(DrumSequencer.getInstance().getSoundButtonList().get(row).get(0).getSound().getSoundName());
            velocitySlider.setShowTickLabels(true);
            velocitySlider.setShowTickMarks(true);

            container.getChildren().add(label);

            velocitySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
                rowAndNewVelocity.put(row, newValue.intValue());
            });
            container.getChildren().add(velocitySlider);
        }

        CommandDialog.showAlert(owner, Alert.AlertType.CONFIRMATION, "Adjust Velocities", "Adjust the velocity for each sound:", "", container, ButtonType.OK, ButtonType.CANCEL)
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        rowAndNewVelocity.forEach((row, velocity) -> {
                            command.setRow(row);
                            command.setNewVelocity(velocity);
                            command.execute();
                        });
                    }
                });
    }
}
