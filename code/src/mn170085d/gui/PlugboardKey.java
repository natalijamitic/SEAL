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
        this.matched = false;
        this.pair = null;
        if (this.myCircle != null) {
            this.myCircle.setFill(Globals.DEFAULT_PAINT);
        }
    }

    public void match(PlugboardKey pair, Paint color) {
        this.matched = true;
        this.pair = pair;
        this.myCircle.setFill(color);
    }

    public Circle getMyCircle() {
        return myCircle;
    }

    public void setMyCircle(Circle myCircle) {
        this.myCircle = myCircle;
    }

    public char getId() {
        return id;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

    public PlugboardKey getPair() {
        return pair;
    }

    public void setPair(PlugboardKey pair) {
        this.pair = pair;
    }
}
