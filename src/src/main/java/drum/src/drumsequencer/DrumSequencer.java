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
    private int sleep = 1000;
    private int sleepTimeMilliseconds = 1000; // Default sleep time

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    private static DrumSequencer instance = new DrumSequencer(false, true);

    public static DrumSequencer getInstance(){
        return instance;
    }


    public void setSoundButtonList(List<List<SoundButton>> soundButtonList) {
        this.soundButtonList = soundButtonList;
    }

    private DrumSequencer(boolean isOn, boolean isClear) {
        this.isOn = isOn;
        this.isClear = isClear;
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
                        Thread.sleep(sleepTimeMilliseconds); // Use sleepTimeMilliseconds variable here
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        playThread.start();

    }



    public void playMidiFile(List<String> filePaths, List<Integer> velocities, List<Double> durations) { // think of patterns
        try {
            for (int i = 0; i < filePaths.size(); i++) {
                String fileName = filePaths.get(i);
                int velocity = velocities.get(i); // Get the corresponding velocity
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

                // Schedule sequencer closure after playback finishes
                scheduler.schedule(new Runnable() { // create a seperate class for this
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

    // Iterate over each track in the sequence
    for (int trackNumber = 0; trackNumber < sequence.getTracks().length; trackNumber++) {
        Track track = sequence.getTracks()[trackNumber];

        // Iterate over each event in the track
        for (int i = 0; i < track.size(); i++) {
            MidiEvent event = track.get(i);
            MidiMessage message = event.getMessage();

            if (message instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) message;

                // Check if the message is a Note On message with velocity > 0
                if (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0) {
                    // Change the velocity
                    try {
                        sm.setMessage(sm.getCommand(), sm.getChannel(), sm.getData1(), velocity);
                        if (durationFactor <= 50){
                            sleep -= 300;
                        }
                        else{
                            sleep += 300;
                        }
                        // Adjust the duration
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

    public enum TimeSignitureEnum{
        fourbyfour,
        eightbysixte
    }

    public enum TimeSignatureEnum {
        FOUR_BY_FOUR("4X4"),
        EIGHT_BY_SIXTEEN("8X16");

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
                TimeSignatureEnum.FOUR_BY_FOUR,
                TimeSignatureEnum.EIGHT_BY_SIXTEEN
        );

        ComboBox<TimeSignatureEnum> timeSignatureComboBox = new ComboBox<>(timeSignatureOptions);
        timeSignatureComboBox.setValue(TimeSignatureEnum.FOUR_BY_FOUR); // Default time signature value

        // Add listener to handle time signature changes
        timeSignatureComboBox.setOnAction(event -> {
            TimeSignatureEnum selectedTimeSignature = timeSignatureComboBox.getValue();
            // Handle time signature change
            switch (selectedTimeSignature) {
                case FOUR_BY_FOUR:
                    sleepTimeMilliseconds = 1000; // Set sleep time to 1000 milliseconds for 4/4 time signature
                    break;
                case EIGHT_BY_SIXTEEN:
                    sleepTimeMilliseconds = 500; // Set sleep time to 500 milliseconds for 8/16 time signature
                    break;
                default:
                    // Handle other time signatures if needed
            }
            System.out.println("Selected Time Signature: " + selectedTimeSignature.getDisplayName());
        });

        return timeSignatureComboBox;
    }

}