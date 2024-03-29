package drum.src.sound;


/**
 * The Sound class holds information necessary to play the sound, the name of the sound, the file path
 * to the audio file, and parameters velocity and duration. It allows for dynamic adjustment of velocity and
 * duration.
 */
public class Sound {
    private String soundName;
    private String soundFile;
    private int velocity = 50;
    private double duration = 50;

    /**
     * Constructs a new Sound object with specified name and file path.
     *
     * @param soundName The name of the sound.
     * @param soundFile File's Path from Repository Root
     */
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


    public int getVelocity() {return velocity;}
    public double getDuration() {
        return duration;
    }

    public synchronized void changeVelocity(int velocity) {
        this.velocity = velocity;
    }
    public void changeDuration(int duration) {
        this.duration = duration;
    }





}

