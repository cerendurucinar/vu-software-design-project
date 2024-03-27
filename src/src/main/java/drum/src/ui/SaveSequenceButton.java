package drum.src.ui;

import drum.src.drumsequencer.DrumSequence;
import drum.src.drumsequencer.DrumSequencer;
import javafx.scene.control.Button;
import javafx.stage.Window;

public class SaveSequenceButton extends AbstractButton {
    private Button button;
    private DrumSequence sequence; // update the class diagram if it is like this


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
