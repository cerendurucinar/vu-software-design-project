package drum.src.observer;

/**
 * This is an Observer interface of the Observer Design Pattern
 * The specific usage for this application:
 * This pattern is used for whenever the state of the SoundButton is changed from triggered
 * to not triggered or vice versa, DrumSequence will be notified and take necessary actions
 * (updates the list accordingly).
 *
 */
public interface Observer {
    public void update(int row, int col, boolean triggerState);

}
