package drum.src.sound;

import drum.src.ui.SoundButton;

import java.util.List;

public class Sound implements SoundController{
    private String soundName;
    private String soundFile;
    private int velocity = 50;
    private double duration = 50;
    public Sound(String soundName, String soundFile){
        this.soundFile = soundFile;
        this.soundName = soundName;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public String getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }

    public int getVelocity() {
        return velocity;
    }

    public synchronized void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    @Override
    public synchronized void changeVelocity(int velocity) {
        this.velocity = velocity;
    }

    @Override
    public void changeDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean checkVelocity(int velocity) {
        boolean retVal = false;
        if (velocity >= 0 && velocity <= 100){
            retVal = true;
        }
        return retVal;
    }

    public double getDuration() {
        return duration;
    }



}

