package drum.src.ui;

import drum.src.drumsequencer.*;
import javafx.scene.control.Button;


/**
 * SaveSequenceButton is a  UI component class, dedicated to saving
 * the current drum sequence.
 */

public class SaveSequenceButton extends AbstractButton {
    private DrumSequence sequence;

    public SaveSequenceButton(String buttonText, DrumSequence seq) {
        this.button = new Button(buttonText);
        this.button.setOnAction(e -> onClick());
        this.sequence = seq;
    }
    @Override
    public void onClick() {
        sequence.saveSequence();
    }

    public Button getFxButton() {
        return button;
    }
}
