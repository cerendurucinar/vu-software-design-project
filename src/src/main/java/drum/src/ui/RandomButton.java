package drum.src.ui;

import drum.src.drumsequencer.*;
import javafx.scene.control.Button;
public class RandomButton extends AbstractButton{
    private DrumSequence seq;
    public RandomButton(String buttonText, DrumSequence seq) {
        this.button = new Button(buttonText);
        this.button.setOnAction(e -> onClick());
        this.seq = seq;
    }

    @Override
    public void onClick() {
        seq.createRandomSequence();
    }

    public Button getFxButton() {
        return button;
    }
}
