package drum.src.ui;


import drum.src.observer.Subject;
import drum.src.sound.Sound;
import drum.src.drumsequencer.DrumSequence;
import drum.src.sound.SoundFactory;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

/**
 * SoundButton is a UI component class that extends AbstractButton and implements the Subject interface.
 * Each button is associated with a specific sound and knows its position within a grid layout through
 * row and column indices. It also participates in the Observer pattern as a Subject to notify its observer, DrumSequence, of state changes.
 */




public class SoundButton extends AbstractButton implements Subject<DrumSequence> {
    private Button button;
    private int row;
    private int col;

    private Sound sound;
    private DrumSequence observer;

    // Animation effect to give feedback to the user on button interaction.
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), this.button);


    /**
     * Constructor:
     *
     * @param buttonText The text to be displayed on the button
     * @param soundName  The name of the sound linked with the button (keys of the sounds Map in SoundFactory)
     * @param row        The row index in the grid layout where this button is located
     * @param col        The column index in the grid layout where this button is located
     */

    public SoundButton(String buttonText, String soundName, int row, int col){
        this.sound = SoundFactory.getSound(soundName); // Use factory
        this.button = new Button(soundName);
        this.row = row;
        this.col = col;

        this.button.setOnAction(e -> onClick());

        button.setStyle("-fx-background-color: lightblue");
        button.setMinWidth(100);
        button.setMinHeight(100);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(0.98);
        scaleTransition.setToY(0.98);
        scaleTransition.setAutoReverse(true);
    }


public void setSound(Sound newSound){
    this.sound = newSound;
    button.setText(newSound.getSoundName());
}

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    public Button getBtn() {
        return button;
    }

    public Sound getSound() {
        return sound;
    }

    @Override
    public void onClick() {
        if (this.getIsTriggered()) {
            scaleTransition.setRate(scaleTransition.getRate() * -1);
            scaleTransition.play();
            button.setStyle("-fx-background-color: lightblue;");
            this.setIsTriggered(false);
            notifyObserver();
        } else {
            scaleTransition.setRate(1);
            scaleTransition.play();
            this.setIsTriggered(true);
            button.setStyle("-fx-background-color: #0e6f8d;");
            notifyObserver();
        }
    }

    @Override
    public void setObserver(DrumSequence observer) {
        this.observer = observer;
    }

    @Override
    public void notifyObserver() {
        observer.update(getRow(), getColumn(), true);
    }
}

