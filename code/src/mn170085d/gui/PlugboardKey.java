package mn170085d.gui;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import mn170085d.Globals;

public class PlugboardKey {
    private char id;
    private Boolean matched = false;
    private PlugboardKey pair = null;
    private Circle myCircle = null;

    public PlugboardKey(char id) {
        this.id = id;
    }

    public void unmatch() {
        matched = false;
        pair = null;
        if (myCircle != null) {
            myCircle.setFill(Globals.PLUGBOARD_GRADIENT);
        }
    }

    public void match(PlugboardKey newPair, Paint color) {
        matched = true;
        pair = newPair;
        myCircle.setFill(color);
    }

    public Circle getMyCircle() {
        return myCircle;
    }

    public void setMyCircle(Circle newCircle) {
        myCircle = newCircle;
    }

    public char getId() {
        return id;
    }

    public boolean isMatched() {
        return matched;
    }

    public PlugboardKey getPair() {
        return pair;
    }
}
