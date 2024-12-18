package drum.src.drumsequencer;


import drum.src.ui.SoundButton;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 *  This class handles the playing and stopping the beat logic. The class is designed with Singleton
 *   pattern because we want our DrumSequencer to be unique to not have some conflicts when playing.
 */
public class DrumSequencer {

    private boolean isOn;
    private List<List<SoundButton>> soundButtonList = new ArrayList<>();
    public static Synthesizer synthesizer;
    private int sleep = 1000;
    private int sleepTimeMilliseconds = 1000;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    // Singleton instance
    private static DrumSequencer instance = new DrumSequencer(false);

    // Private constructor to enforce singleton pattern.
    private DrumSequencer(boolean isOn) {
        this.isOn = isOn;
    }

    // Public method to access the singleton instance.
    public static DrumSequencer getInstance(){
        return instance;
    }


    public void setSoundButtonList(List<List<SoundButton>> soundButtonList) {
        this.soundButtonList = soundButtonList;
    }



    public void play(){
        Thread playThread = new Thread(() -> {
            while (isOn) {
                for (int c = 0; c < soundButtonList.get(0).size(); c++) {
                    List<Integer> velocities = new ArrayList<>();
                    List<String> notesToPlay = new ArrayList<>();
                    List<Double> durations = new ArrayList<>();
                    for (int r = 0; r < soundButtonList.size(); r++) {

                        if (soundButtonList.get(r).get(c).getIsTriggered()) {
                            SoundButton sound = soundButtonList.get(r).get(c);
                                notesToPlay.add(sound.getSound().getSoundFile());
                                velocities.add(sound.getSound().getVelocity());
                                durations.add(sound.getSound().getDuration());
                        }
                    }

                    for (int r = 0; r < soundButtonList.size(); r++) {
                          Button button = soundButtonList.get(r).get(c).getBtn();
//                        button.setScaleX(1.05);
//                        button.setScaleY(1.05);
                        Platform.runLater(() -> {
                            button.setScaleX(1.10);
                            button.setScaleY(1.10);
                        });
                    }
                    playMidiFile(notesToPlay, velocities,durations);

                    for (int r = 0; r < soundButtonList.size(); r++) {
                          Button button = soundButtonList.get(r).get(c).getBtn();
//                        if (soundButtonList.get(r).get(c).getIsTriggered()) {
//                            button.setScaleX(0.98);
//                            button.setScaleY(0.98);
//                        } else {
//                            button.setScaleX(1);
//                            button.setScaleY(1);
//                        }
                        int finalR = r;
                        int finalC = c;
                        Platform.runLater(() -> {
                            if (soundButtonList.get(finalR).get(finalC).getIsTriggered()) {
                                button.setScaleX(0.98);
                                button.setScaleY(0.98);
                            } else {
                                button.setScaleX(1);
                                button.setScaleY(1);
                            }
                        });

                    }
                    try {
                        if (!isOn) { // to ensure that outer for loop terminates if play button is clicked
                            break;
                        }
                        Thread.sleep(sleepTimeMilliseconds);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        playThread.start();

    }



    public void playMidiFile(List<String> filePaths, List<Integer> velocities, List<Double> durations) {
        try {
            for (int i = 0; i < filePaths.size(); i++) {
                String fileName = filePaths.get(i);
                int velocity = velocities.get(i);
                double duration = durations.get(i);

                File file = new File(fileName);
                if (!file.exists()) {
                    System.err.println("File not found: " + fileName);
                    continue;
                }

                Sequence sequence = MidiSystem.getSequence(file);
                adjustSequenceForVelocityAndDuration(sequence, velocity, duration);
                Sequencer sequencer = MidiSystem.getSequencer();

                sequencer.open();
                sequencer.setSequence(sequence);
                sequencer.start();

                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        while (sequencer.isRunning()) {
                            try {
                                Thread.sleep(sleep);
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

private void adjustSequenceForVelocityAndDuration(Sequence sequence, int velocity, double durationFactor) {

    for (int trackNumber = 0; trackNumber < sequence.getTracks().length; trackNumber++) {
        Track track = sequence.getTracks()[trackNumber];

        for (int i = 0; i < track.size(); i++) {
            MidiEvent event = track.get(i);
            MidiMessage message = event.getMessage();

            if (message instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) message;

                if (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0) {
                    try {
                        sm.setMessage(sm.getCommand(), sm.getChannel(), sm.getData1(), velocity);
                        if (durationFactor <= 50){
                            sleep -= 300;
                        }
                        else{
                            sleep += 300;
                        }
                        long tick = event.getTick();
                        long duration = (long) durationFactor;
                        event.setTick(tick + duration);
                    } catch (InvalidMidiDataException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

    public void clearSequence(){
        Iterator<List<SoundButton>> rowIterator = soundButtonList.iterator();
        while(rowIterator.hasNext()){
            List<SoundButton> sbtnList = rowIterator.next();
            Iterator<SoundButton> colIterator = sbtnList.iterator();
            while(colIterator.hasNext()){
                SoundButton sbtn = colIterator.next();
                if (sbtn.getIsTriggered()) {
                    sbtn.onClick();
                }
            }
        }

    }

    public  void setOn(boolean on) {
        isOn = on;
    }

    public boolean isOn() {
        return isOn;
    }


    public List<List<SoundButton>> getSoundButtonList() {
        return soundButtonList;
    }

    public enum TimeSignatureEnum {
        Tempo1("4X4"),
        Tempo2("8X16");

        private final String displayName;

        TimeSignatureEnum(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public ComboBox<TimeSignatureEnum> createTimeSignature() {
        ObservableList<TimeSignatureEnum> timeSignatureOptions = FXCollections.observableArrayList(
                TimeSignatureEnum.Tempo1,
                TimeSignatureEnum.Tempo2
        );

        ComboBox<TimeSignatureEnum> timeSignatureComboBox = new ComboBox<>(timeSignatureOptions);
        timeSignatureComboBox.setValue(TimeSignatureEnum.Tempo1);

        timeSignatureComboBox.setOnAction(event -> {
            TimeSignatureEnum selectedTimeSignature = timeSignatureComboBox.getValue();

            switch (selectedTimeSignature) {
                case Tempo1:
                    sleepTimeMilliseconds = 1000;
                    break;
                case Tempo2:
                    sleepTimeMilliseconds = 500;
                    break;
                default:

            }
            System.out.println("Selected Time Signature: " + selectedTimeSignature.getDisplayName());
        });

        return timeSignatureComboBox;
    }

}