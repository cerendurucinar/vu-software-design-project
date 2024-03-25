package drum.src.observer;

public interface Subject<T> {
    public void setObserver(T observer);
    public void notifyObserver();
}
