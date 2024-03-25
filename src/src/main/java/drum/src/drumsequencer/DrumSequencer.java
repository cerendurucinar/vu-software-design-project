package drum.src.drumsequencer;

import drum.src.ui.SoundButton;
import javafx.scene.control.Button;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.sound.midi.Sequence;

public class DrumSequencer { // TODO:rename
    // TODO: check Singleton
    private boolean isOn;
    private boolean isClear;
    private List<List<SoundButton>> soundButtonList = new ArrayList<>();
    public static Synthesizer synthesizer;

    MidiChannel[] channels = synthesizer.getChannels();

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);





    public DrumSequencer(boolean isOn, boolean isClear, List<List<SoundButton>> soundButtonList) {
        this.isOn = isOn;
        this.isClear = isClear;
        this.soundButtonList = soundButtonList;
    }

    public void play(){
        Thread playThread = new Thread(() -> {
            while (isOn) {
                for (int c = 0; c < soundButtonList.get(0).size(); c++) {

                    List<String> notesToPlay = new ArrayList<>();
                    for (int r = 0; r < soundButtonList.size(); r++) {

                        if (soundButtonList.get(r).get(c).getIsTriggered()) {
                            Sound sound = soundButtonList.get(r).get(c).getSound();
                                notesToPlay.add(sound.getSoundFile());

                        }
                    }

                    for (int r = 0; r < soundButtonList.size(); r++) {
                        Button button = soundButtonList.get(r).get(c).getBtn();
                        button.setScaleX(1.05);
                        button.setScaleY(1.05);
                    }
                    playMidiFile(notesToPlay);

                    for (int r = 0; r < soundButtonList.size(); r++) {
                        Button button = soundButtonList.get(r).get(c).getBtn();
                        if (soundButtonList.get(r).get(c).getIsTriggered()) {
                            button.setScaleX(0.98);
                            button.setScaleY(0.98);
                        } else {
                            button.setScaleX(1);
                            button.setScaleY(1);
                        }

                    }
                    try {
                        if (!isOn) { // to ensure that outer for loop terminates if play button is clicked
                            break;
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        playThread.start();

    }

    private void playMidiSounds(List<String> filePaths) {


        if (synthesizer != null) {
            MidiChannel channel = channels[0];

            try {
                for (String fileName : filePaths) {
                    File file = new File("src/src/main/java/drum/src/" + fileName);
                    if (!file.exists()) {
                        System.err.println("File not found: " + fileName);
                        continue;
                    }

                    javax.sound.midi.Sequence sequence = MidiSystem.getSequence(file);

                    Sequencer sequencer = MidiSystem.getSequencer();
                    sequencer.open();
                    sequencer.setSequence(sequence);
                    sequencer.start();

                    while (sequencer.isRunning()) {
                        Thread.sleep(1000);
                    }

                    sequencer.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void playMidiFile(List<String> filePaths) { // think of patterns
        try {
            for (String fileName : filePaths) {
                File file = new File( fileName);
                if (!file.exists()) {
                    System.err.println("File not found: " + fileName);
                    continue;
                }

                Sequence sequence = MidiSystem.getSequence(file);

                Sequencer sequencer = MidiSystem.getSequencer();

                sequencer.open();
                sequencer.setSequence(sequence);
                sequencer.start();

                // Schedule sequencer closure after playback finishes
                scheduler.schedule(new Runnable() { // create a seperate class for this
                    @Override
                    public void run() {
                        while (sequencer.isRunning()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        sequencer.close();
                    }
                }, 0, TimeUnit.SECONDS);

            }
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        if (DrumSequencer.synthesizer != null && DrumSequencer.synthesizer.isOpen()) {
            DrumSequencer.synthesizer.close();
        }
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
    public void clearSequence(){
        for(int r = 0; r<soundButtonList.size(); r++){
            for(int c = 0; c<soundButtonList.get(r).size(); c++){
                if (soundButtonList.get(r).get(c).getIsTriggered()) {
                    soundButtonList.get(r).get(c).onClick();
                }
            }
        }
    }

    public  void setOn(boolean on) {
        isOn = on;
    }

    public void setClear(boolean clear) {
        isClear = clear;
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean isClear() {
        return isClear;
    }

    public List<List<SoundButton>> getSoundButtonList() {
        return soundButtonList;
    }
}