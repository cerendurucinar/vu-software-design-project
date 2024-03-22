package drum.src.ui;

import javafx.scene.control.Button;

public class RandomButton extends AbstractButton{
    private Button button;
    private Sequence seq;


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
