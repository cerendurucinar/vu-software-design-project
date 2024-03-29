package drum.src.observer;


/**
 * This is an Subject interface of the Observer Design Pattern
 * The specific usage for this application:
 * This pattern is used for whenever the state of the SoundButton is changed from triggered
 * to not triggered or vice versa, DrumSequence will be notified and take necessary actions
 *
 * @param <T> The type of the observer, allowing for type-safe references to observers.
 */

public interface Subject<T> {
    public void setObserver(T observer);
    public void notifyObserver();
}
