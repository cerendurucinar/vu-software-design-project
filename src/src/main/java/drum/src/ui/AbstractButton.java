package drum.src.ui;

import drum.src.drumsequencer.DrumSequencer;

import java.awt.*;

public abstract class AbstractButton {
    boolean isTriggered = false;
    DrumSequencer seq;

    public abstract void onClick();



    public boolean getIsTriggered() {
        return isTriggered;
    }

    public void setIsTriggered(boolean triggered) {
        isTriggered = triggered;
    }
}
