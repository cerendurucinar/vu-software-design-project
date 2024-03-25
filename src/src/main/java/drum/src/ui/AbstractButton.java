package drum.src.ui;

import drum.src.drumsequencer.Drum_Sequencer;

public abstract class AbstractButton {
    boolean isTriggered = false;
    Drum_Sequencer seq;

    public abstract void onClick();

    public boolean getIsTriggered() {
        return isTriggered;
    }

    public void setIsTriggered(boolean triggered) {
        isTriggered = triggered;
    }
}
