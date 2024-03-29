package drum.src.ui;

import drum.src.drumsequencer.*;
import javafx.scene.control.Button;

/**
 * RandomButton is a UI component that initiates the creation of
 * a random drum sequence within the application.
 */
public class RandomButton extends AbstractButton{
    private DrumSequence sequence;
    public RandomButton(String buttonText, DrumSequence sequence) {
        this.button = new Button(buttonText);
        this.button.setOnAction(e -> onClick());
        this.sequence = sequence;
    }

    @Override
    public void onClick() {
        sequence.createRandomSequence();
    }

    public Button getFxButton() {
        return button;
    }
}
