package mn170085d.gui;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import mn170085d.Globals;
import mn170085d.enigma.Machine;

public class Keyboard {
    private static final int CLICK_TRANSLATION = 2;
    private Machine machine;
    private Label inputLabel, outputLabel;
    private PauseTransition lampboardTransition;
    private char activeKey;
    private StackPane activePane, delStackPane;

    public Keyboard(Machine machine, Label inputLabel, Label outputLabel, StackPane delStackPane) {
        this.machine = machine;
        this.inputLabel = inputLabel;
        this.outputLabel = outputLabel;
        this.delStackPane = delStackPane;
    }

    public void pressedKey(KeyEvent e, Scene scene) {
        StackPane keyboard = (StackPane)scene.lookup("#keyboardKey" + e.getText().toUpperCase());
        selectKey(keyboard);
    }

    public void selectKey(StackPane stackPane) {
        stackPane.setTranslateY(CLICK_TRANSLATION);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(event -> stackPane.setTranslateY(0));
        pause.play();

        char key = stackPane.getId().charAt(stackPane.getId().length() - 1);
        char output = machine.cryptText(Character.toString(key)).charAt(0);

        toggleLampboardKey(output, stackPane, true);
        lampboardTransition = new PauseTransition(Duration.seconds(1));
        lampboardTransition.setOnFinished(event -> toggleLampboardKey(output, stackPane, false));
        lampboardTransition.play();

        if (inputLabel.getText().length() == 79) {
            deleteLabels();
        }
        inputLabel.setText(inputLabel.getText() + key);
        outputLabel.setText(outputLabel.getText() + output);
    }

    public void setMachine(Machine newMachine) {
        machine = newMachine;
    }

    public void deleteLabels(){
        delStackPane.setTranslateY(CLICK_TRANSLATION * 2);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(event -> {
            inputLabel.setText("");
            outputLabel.setText("");
            delStackPane.setTranslateY(0);
        });
        pause.play();
    }

    private void stopPreviousLampboardKey(){
        if (lampboardTransition != null && lampboardTransition.getStatus() == Animation.Status.RUNNING) {
            toggleLampboardKey(activeKey, activePane, false);
        }
    }

    private void toggleLampboardKey(char key, StackPane stackPane, boolean turnOn) {
        if (turnOn) {
            stopPreviousLampboardKey();
            activeKey = key;
            activePane = stackPane;
        }

        StackPane lampboard = (StackPane)stackPane.getScene().lookup("#lampboardKey" + key);
        lampboard.getChildren().forEach(child -> {
            if (child instanceof Circle) {
                ((Circle) child).setFill(turnOn ? Globals.LAMPBOARD_ON_FILL : Globals.LAMPBOARD_OFF_FILL);
                ((Circle) child).setStroke(turnOn ? Globals.LAMPBOARD_ON_STROKE : Globals.LAMPBOARD_OFF_STROKE);
            } else if (child instanceof Label) {
                ((Label) child).setTextFill(turnOn ? Globals.LAMPBOARD_ON_TEXT_FILL : Globals.LAMPBOARD_OFF_TEXT_FILL);
            }
        });
    }
}
