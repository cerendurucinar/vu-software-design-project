package drum.src.ui;


import drum.src.observer.*;
import drum.src.sound.*;
import drum.src.drumsequencer.*;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;


public class SoundButton extends AbstractButton implements Subject<DrumSequence> {
    //private Button button;
    private int row;
    private int col;

    private Sound sound;
    private DrumSequence observer;

    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), this.button);
    public SoundButton(String buttonText, String soundName, int row, int col){
        this.button = new Button(buttonText);
        this.sound = SoundFactory.getSound(soundName); // Use factory
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
   // button.setText(newSound.getSoundName());
}

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return col;
    }

    public void setColumn(int col) {
        this.col = col;
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

