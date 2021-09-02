package mn170085d.gui;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import mn170085d.Globals;
import mn170085d.enigma.Rotor;

import java.util.*;


public class LineDrawer {
    private List<Line> allLines = new ArrayList<>();
    private Label plugboardLeft, plugboardRight, leftRotorLeft, leftRotorRight, middleRotorLeft, middleRotorRight, rightRotorLeft, rightRotorRight, reflectorRight;
    private Pane drawingPane;
    private int reflectorWireCounter = 1;
    private Set<Character> drawnReflectorWires = new HashSet<>();
    private Map<String, List<Line>> linesMap = new HashMap<>();

    private Label inputSimulationLabel, outputSimulationLabel;

    public LineDrawer(Label plugboardLeft, Label plugboardRight, Label leftRotorLeft, Label leftRotorRight, Label middleRotorLeft, Label middleRotorRight, Label rightRotorLeft, Label rightRotorRight, Label reflectorRight, Pane drawingPane, Label inputSimulationLabel, Label outputSimulationLabel) {
        this.plugboardLeft = plugboardLeft;
        this.plugboardRight = plugboardRight;
        this.leftRotorLeft = leftRotorLeft;
        this.leftRotorRight = leftRotorRight;
        this.middleRotorLeft = middleRotorLeft;
        this.middleRotorRight = middleRotorRight;
        this.rightRotorLeft = rightRotorLeft;
        this.rightRotorRight = rightRotorRight;
        this.reflectorRight = reflectorRight;
        this.drawingPane = drawingPane;
        this.inputSimulationLabel = inputSimulationLabel;
        this.outputSimulationLabel = outputSimulationLabel;
    }

    private class TypeSpecificLabels {
        private Label source;
        private Label destination;

        public TypeSpecificLabels(Label source, Label destination) {
            this.source = source;
            this.destination = destination;
        }
    }

    public TypeSpecificLabels getLabelsForType(int type) {
        switch(type) {
            case Globals.FAST_ROTOR:
                return new TypeSpecificLabels(rightRotorRight, rightRotorLeft);
            case Globals.MID_ROTOR:
                return new TypeSpecificLabels(middleRotorRight, middleRotorLeft);
            case Globals.SLOW_ROTOR:
                return new TypeSpecificLabels(leftRotorRight, leftRotorLeft);
            case Globals.PLUGBOARD:
                return new TypeSpecificLabels(plugboardRight, plugboardLeft);
            case Globals.REFLECTOR:
                return new TypeSpecificLabels(reflectorRight, reflectorRight);
            default: return null;
        }
    }

    private static int LEFT_ADJUSTMENT = 20;
    private static int RIGHT_ADJUSTMENT = 10;
    private static int TOP_ADJUSTMENT = 12;
    private static int ROW_ADJUSTMENT = 18;
    private static int REFLECTOR_HEIGHT_ADJUSTMENT = 12;

    private Line createLine(int type, char source, char destination) {
        Line line = new Line();
        addDefaultLineCharacteristics(line);

        this.drawingPane.getChildren().add(line);
        allLines.add(line);


        saveLineInMap(type, source, destination, line);
        return line;
    }

    private void saveLineInMap(int type, char source, char destination, Line line) {
        if (!(linesMap.containsKey("" + type + source + destination) || linesMap.containsKey("" + type + source + destination))) {
            linesMap.put("" + type + source + destination, new LinkedList<Line>(){
                {
                    this.add(line);
                }
            });
        } else if (type == Globals.REFLECTOR) {
            if (linesMap.containsKey("" + type + source + destination)) {
                linesMap.get("" + type + source + destination).add(line);
            } else {
                linesMap.get("" + type + destination + source).add(line);
            }
        }
    }

    private void drawLine(int type,  char source, char destination) {
        TypeSpecificLabels labels = getLabelsForType(type);

        int rowSource = getLetterPosition(source, labels.source);
        int rowDestination = getLetterPosition(destination, labels.destination);

        Line line = createLine(type, source, destination);

        Bounds sourceBounds = line.screenToLocal(labels.source.localToScreen(labels.source.getBoundsInLocal()));
        Bounds destinationBounds = line.screenToLocal(labels.destination.localToScreen(labels.destination.getBoundsInLocal()));

        line.setStartX(sourceBounds.getMinX() - RIGHT_ADJUSTMENT);
        line.setStartY(sourceBounds.getMinY() + TOP_ADJUSTMENT + rowSource * ROW_ADJUSTMENT);
        line.setEndX(destinationBounds.getMinX() + LEFT_ADJUSTMENT);
        line.setEndY(destinationBounds.getMinY() + TOP_ADJUSTMENT + rowDestination * ROW_ADJUSTMENT);
    }

    public void drawReflectorLines(int type, char source, char destination) {
        if (drawnReflectorWires.contains(source)) {
            return;
        }
        drawnReflectorWires.add(source);
        drawnReflectorWires.add(destination);

        TypeSpecificLabels labels = getLabelsForType(type);

        int rowSource = source - Globals.A_CODE;
        int rowDestination = destination - Globals.A_CODE;

        // Vertical Line
        Line line = createLine(type, source, destination);
        Bounds sourceBounds = line.screenToLocal(labels.source.localToScreen(labels.source.getBoundsInLocal()));
        line.setStartX(sourceBounds.getMinX() - RIGHT_ADJUSTMENT - REFLECTOR_HEIGHT_ADJUSTMENT * reflectorWireCounter);
        line.setStartY(sourceBounds.getMinY() + TOP_ADJUSTMENT + rowSource * ROW_ADJUSTMENT);
        line.setEndX(sourceBounds.getMinX()  - RIGHT_ADJUSTMENT - REFLECTOR_HEIGHT_ADJUSTMENT * reflectorWireCounter);
        line.setEndY(sourceBounds.getMinY() + TOP_ADJUSTMENT + rowDestination * ROW_ADJUSTMENT);

        // Horizontal Top Line
        Line lineHorizontalTop = createLine(type, source, destination);
        lineHorizontalTop.setStartX(line.getStartX());
        lineHorizontalTop.setStartY(line.getStartY());
        lineHorizontalTop.setEndX(line.getStartX() + REFLECTOR_HEIGHT_ADJUSTMENT * reflectorWireCounter);
        lineHorizontalTop.setEndY(line.getStartY());

        // Horizontal Bottom Line
        Line lineHorizontalBottom = createLine(type, source, destination);
        lineHorizontalBottom.setStartX(line.getEndX());
        lineHorizontalBottom.setStartY(line.getEndY());
        lineHorizontalBottom.setEndX(line.getEndX() + REFLECTOR_HEIGHT_ADJUSTMENT * reflectorWireCounter);
        lineHorizontalBottom.setEndY(line.getEndY());

        reflectorWireCounter++;
    }

    public void drawLineBetweenLetters(int type, char source, char destination) {
        if (type == Globals.REFLECTOR) {
            drawReflectorLines(type, source, destination);
        } else {
            drawLine(type, source, destination);
        }
    }

    private int getLetterPosition(char letter, Label lettersLabel) {
        String letters = lettersLabel.getText();
        return letters.indexOf(letter);
    }

    public void paintLineBetweenEntries(int type, int source, int destination, Paint paint) {
        TypeSpecificLabels labels = getLabelsForType(type);
        char sourceLetter = labels.source.getText().charAt(source);
        char destinationLetter = labels.destination.getText().charAt(destination);

        List<Line> lines = getLines(type, sourceLetter, destinationLetter);
        if (lines == null) {
            return;
        }

        if (type == Globals.REFLECTOR) {
            paintReflectorLines(sourceLetter, destinationLetter, lines);
            return;
        }
        lines.forEach(line -> {
            addSpecialLineCharacteristic(line, paint);
        });
    }

    private void paintReflectorLines(char sourceLetter, char destinationLetter, List<Line> lines) {
        Line lineVertical = lines.get(0);
        Line lineSource = (destinationLetter > sourceLetter) ? lines.get(1) : lines.get(2);
        Line lineDestination = (destinationLetter < sourceLetter) ? lines.get(1) : lines.get(2);

        Stop stops[] = new Stop[] {
                new Stop(0, Globals.FORWARD_COLOR),
                new Stop(1, Globals.BACK_COLOR)
        };

        LinearGradient gradient = new LinearGradient(lineSource.getStartX(), lineSource.getEndY(), lineDestination.getStartX(), lineDestination.getEndY(), false, CycleMethod.NO_CYCLE, stops);

        addSpecialLineCharacteristic(lineVertical, gradient);
        addSpecialLineCharacteristic(lineSource, Globals.FORWARD_PAINT);
        addSpecialLineCharacteristic(lineDestination, Globals.BACK_PAINT);
    }

    private List<Line> getLines(int type, char source, char destination) {
        String key = null;
        if (linesMap.containsKey("" + type + source + destination)) {
            key = "" + type + source + destination;
        } else if (linesMap.containsKey("" + type + destination + source)) {
            key = "" + type + destination + source;
        }

        if (key == null) {
            return null;
        }

        return linesMap.get(key);
    }

    private String rotateString(String text) {
        char firstLetter = text.charAt(0);
        StringBuilder sb = new StringBuilder(text.substring(1));
        sb.append(firstLetter);
        return sb.toString();
    }

    public void rotate(int type) {
        TypeSpecificLabels labels = getLabelsForType(type);
        labels.source.setText(rotateString(labels.source.getText()));
        labels.destination.setText(rotateString(labels.destination.getText()));
    }

    public void removeAllLines() {
        allLines.forEach(line -> {
            drawingPane.getChildren().remove(line);
        });
        allLines.clear();
        drawnReflectorWires.clear();
        linesMap.clear();
        reflectorWireCounter = 1;

        inputSimulationLabel.setLayoutX(0);
        inputSimulationLabel.setLayoutY(0);
        outputSimulationLabel.setLayoutX(0);
        outputSimulationLabel.setLayoutY(0);
    }

    public void matchWithRotor(Rotor rotor, int type) {
        int offset = rotor.getOffset();
        TypeSpecificLabels labels = getLabelsForType(type);
        int labelOffset = labels.source.getText().charAt(0) - Globals.A_CODE;
        while (offset != labelOffset) {
            rotate(type);
            labelOffset = labels.source.getText().charAt(0) - Globals.A_CODE;
        }
    }

    public void drawOutputLabel(int index) {
        drawLabel(outputSimulationLabel, index);
    }

    public void drawInputLabel(int index) {
        drawLabel(inputSimulationLabel, index);
    }

    private void drawLabel(Label label, int index){
        TypeSpecificLabels labels = getLabelsForType(Globals.PLUGBOARD);

        Bounds bounds = label.screenToLocal(labels.source.localToScreen(labels.source.getBoundsInLocal()));

        label.setVisible(true);
        label.setLayoutX(bounds.getMinX() + LEFT_ADJUSTMENT * 1.5);
        label.setLayoutY(bounds.getMinY() + index * ROW_ADJUSTMENT);
    }

    private void addSpecialLineCharacteristic(Line line, Paint paint) {
        line.setOpacity(1);
        line.setStrokeWidth(2.5);
        line.setStroke(paint);
        line.toFront();
    }

    private void addDefaultLineCharacteristics(Line line) {
        line.setStroke(Globals.DEFAULT_PAINT);
        line.setStrokeWidth(1.2);
        line.setOpacity(0.7);
    }
}
