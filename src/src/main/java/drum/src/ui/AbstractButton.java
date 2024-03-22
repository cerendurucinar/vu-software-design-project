package drum.src.ui;

public abstract class AbstractButton {
    boolean isTriggered = false;
    Sequencer seq;

    public abstract void onClick();

    public boolean getIsTriggered() {
        return isTriggered;
    }

    public void setIsTriggered(boolean triggered) {
        isTriggered = triggered;
    }
}
