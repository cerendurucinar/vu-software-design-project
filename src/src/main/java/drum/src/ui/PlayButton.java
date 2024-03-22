package drum.src.ui;

import javafx.scene.control.Button;

import javax.sound.midi.MidiChannel;
import java.util.ArrayList;
import java.util.List;



    public  class PlayButton extends AbstractButton {
        private Button button;
        private Sequencer seq;
        public PlayButton(String buttonText, Sequencer seq) {
            this.button = new Button(buttonText);
            this.button.setOnAction(e -> onClick());
            this.seq = seq;
        }

        @Override
        public void onClick() {

            setIsTriggered(!getIsTriggered());
            if (getIsTriggered()) {
                button.setText("Stop");
                seq.setOn(true);
                seq.play();
            } else {
                seq.setOn(false);
                button.setText("Start");

            }
        }

        public Button getFxButton() { // Provide access to the JavaFX button for UI integration
            return button;
        }



    }

