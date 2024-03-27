package drum.src.sound;

import drum.src.drumsequencer.DrumSequencer;
import drum.src.ui.SoundButton;

import java.util.List;

public class Sound {
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


    public String getSoundFile() {
        return soundFile;
    }


    public int getVelocity() {
        return velocity;
    }

    public synchronized void changeVelocity(int velocity) {
        this.velocity = velocity;
    }
    public void changeDuration(int duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }



}

