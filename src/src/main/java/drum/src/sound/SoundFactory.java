package drum.src.sound;
import drum.src.ui.SoundButton;

import java.util.*;

public class SoundFactory {

    private static final Map<String, Sound> sounds = new HashMap<>();

    static {
        loadSounds();
    }

    private static void loadSounds() {

        sounds.put("Sound0", new Sound("Sound0", "src/src/main/resources/drum/src/sounds/Sound0.mid"));
        sounds.put("Sound1", new Sound("Sound1", "src/src/main/resources/drum/src/sounds/Sound1.mid"));
        sounds.put("Sound2", new Sound("Sound2", "src/src/main/resources/drum/src/sounds/Sound2.mid"));
        sounds.put("Sound3", new Sound("Sound3", "src/src/main/resources/drum/src/sounds/Sound3.mid"));
    }


   public static Sound getSound(String soundName) {
       return sounds.get(soundName);
   }

    public static void addSound(String soundName, String soundFile) {
        sounds.putIfAbsent(soundName, new Sound(soundName, soundFile));
    }
    public static Set<String> getAllSoundNames() {
        return Collections.unmodifiableSet(sounds.keySet());
    }
    public static void removeSound(String soundName) {
        sounds.remove(soundName);
    }



}
