package drum.src.sound;

public class Sound {
    private String soundName;
    private String soundFile;
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
}
