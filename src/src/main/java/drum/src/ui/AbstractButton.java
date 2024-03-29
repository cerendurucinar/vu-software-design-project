package drum.src.ui;

import javafx.scene.control.Button;


/**
 * AbstractButton provides a base class for various button components within the application.
 * It contains a flag isTriggered for toggle behaviour of buttons and a JavaFX Button for
 * graphical representation of the button.
 */
public abstract class AbstractButton {
    Button button;
    boolean isTriggered = false;


    public abstract void onClick();

    public boolean getIsTriggered() {
        return isTriggered;
    }

    public void setIsTriggered(boolean triggered) {
        isTriggered = triggered;
    }
}
