package drum.src.ui;

import drum.src.drumsequencer.Drum_Sequencer;
import javafx.scene.control.Button;


public  class PlayButton extends AbstractButton {
        private Button button;
        private Drum_Sequencer seq;
        public PlayButton(String buttonText, Drum_Sequencer seq) {
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

