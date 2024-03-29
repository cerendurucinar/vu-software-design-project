package drum.src.ui;

import drum.src.drumsequencer.*;
import javafx.scene.control.Button;



/**
 * ClearButton PlayButton is a UI component that, when triggered, clears the drum sequence in the application. It inherits from StateButton, suggesting it might
 */
public class ClearButton extends StateButton {



    public ClearButton(String buttonText, DrumSequencer seq) {
        this.button = new Button(buttonText);
        this.button.setOnAction(e -> onClick());
        //this.seq = seq;
    }

    @Override
    public void onClick() {DrumSequencer.getInstance().clearSequence();}

    public Button getFxButton() { // Provide access to the JavaFX button for UI integration
        return button;
    }
}
