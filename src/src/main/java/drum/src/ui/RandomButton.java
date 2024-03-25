package drum.src.ui;

import drum.src.sequencer.Sequence;
import javafx.scene.control.Button;



public class RandomButton extends AbstractButton{
    private Button button;
    private Sequence seq; // update the class diagram if it is like this


    public RandomButton(String buttonText, Sequence seq) {
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
