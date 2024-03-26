package drum.src.sound;

public interface SoundController {
    public void changeVelocity(int velocity);
    public void changeDuration(int duration);
    public boolean checkVelocity(int velocity);
}
