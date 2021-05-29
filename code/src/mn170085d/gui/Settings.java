package mn170085d.gui;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import mn170085d.Globals;
import mn170085d.enigma.Machine;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    private static final String colors[] = {"ff99c8", "fcf6bd", "5d2e46", "b58db6", "f79d84", "d0f4de", "fac05e", "3fa7d6", "a9def9", "e4c1f9", "59cd90", "ee6352", "3a5a40"};
    private List<PlugboardKey> allPlugboardKeys = new ArrayList<>();
    private List<Paint> freeColors = new ArrayList<>();
    private Paint currentColor = null;
    private PlugboardKey currentPlugboardKey = null;
    private Machine machine;

    private ChoiceBox<String> reflectorTypes, leftRotorTypes, middleRotorTypes, rightRotorTypes, leftRotorStart, middleRotorStart, rightRotorStart, leftRotorRing, middleRotorRing, rightRotorRing;

    public Settings(Machine machine, ChoiceBox<String> reflectorTypes, ChoiceBox<String> leftRotorTypes, ChoiceBox<String> middleRotorTypes, ChoiceBox<String> rightRotorTypes, ChoiceBox<String> leftRotorStart, ChoiceBox<String> middleRotorStart, ChoiceBox<String> rightRotorStart, ChoiceBox<String> leftRotorRing, ChoiceBox<String> middleRotorRing, ChoiceBox<String> rightRotorRing) {
        this.machine = machine;
        this.reflectorTypes = reflectorTypes;
        this.leftRotorTypes = leftRotorTypes;
        this.middleRotorTypes = middleRotorTypes;
        this.rightRotorTypes = rightRotorTypes;
        this.leftRotorStart = leftRotorStart;
        this.middleRotorStart = middleRotorStart;
        this.rightRotorStart = rightRotorStart;
        this.leftRotorRing = leftRotorRing;
        this.middleRotorRing = middleRotorRing;
        this.rightRotorRing = rightRotorRing;
    }

    public void resetRotorsAndReflector() {
        this.reflectorTypes.setItems(FXCollections.observableArrayList(Globals.reflectorTypes));
        this.reflectorTypes.setValue(Globals.reflectorTypes[0]);
        this.leftRotorTypes.setItems(FXCollections.observableArrayList(Globals.rotorTypes));
        this.leftRotorTypes.setValue(Globals.rotorTypes[0]);
        this.leftRotorStart.setItems(FXCollections.observableArrayList(Globals.alphabet));
        this.leftRotorStart.setValue(Globals.alphabet[0]);
        this.leftRotorRing.setItems(FXCollections.observableArrayList(Globals.alphabet));
        this.leftRotorRing.setValue(Globals.alphabet[0]);
        this.middleRotorTypes.setItems(FXCollections.observableArrayList(Globals.rotorTypes));
        this.middleRotorTypes.setValue(Globals.rotorTypes[0]);
        this.middleRotorStart.setItems(FXCollections.observableArrayList(Globals.alphabet));
        this.middleRotorStart.setValue(Globals.alphabet[0]);
        this.middleRotorRing.setItems(FXCollections.observableArrayList(Globals.alphabet));
        this.middleRotorRing.setValue(Globals.alphabet[0]);
        this.rightRotorTypes.setItems(FXCollections.observableArrayList(Globals.rotorTypes));
        this.rightRotorTypes.setValue(Globals.rotorTypes[0]);
        this.rightRotorStart.setItems(FXCollections.observableArrayList(Globals.alphabet));
        this.rightRotorStart.setValue(Globals.alphabet[0]);
        this.rightRotorRing.setItems(FXCollections.observableArrayList(Globals.alphabet));
        this.rightRotorRing.setValue(Globals.alphabet[0]);
    }

    public void selectPlugboardKey(Event e) {
        StackPane stackPane = (StackPane)e.getSource();
        char plugboardKeyId = stackPane.getId().charAt(stackPane.getId().length() - 1);
        int plugboardKeyIndex = (int)plugboardKeyId - Globals.A_CODE;
        PlugboardKey plugboardKey = this.allPlugboardKeys.get(plugboardKeyIndex);
        Circle plugboardKeyCircle = (Circle)(stackPane.getChildren().get(0));
        if (plugboardKey.getMyCircle() == null) {
            plugboardKey.setMyCircle(plugboardKeyCircle);
        }

        if (currentPlugboardKey == null) {
            // if I wasn't matched then remember me for next matching
            // else if I was matched then unmatch me now from my pair
            if (plugboardKey.isMatched()) {
                PlugboardKey plugboardPair = plugboardKey.getPair();
                plugboardPair.unmatch();

                freeColors.add(plugboardKey.getMyCircle().getFill());
                plugboardKey.unmatch();
            }

            currentPlugboardKey = plugboardKey;
            currentColor = freeColors.remove(0);
            plugboardKeyCircle.setFill(currentColor);
        } else {
            // if clicked on myself again - unselect myself
            // else I have a new match (check if my new match was already matched or not)
            if (currentPlugboardKey.equals(plugboardKey)) {
                plugboardKeyCircle.setFill(Globals.DEFAULT_PAINT);
            } else {
                if (plugboardKey.isMatched()) {
                    PlugboardKey plugboardPair = plugboardKey.getPair();
                    freeColors.add(plugboardPair.getMyCircle().getFill());
                    plugboardPair.unmatch();

                    plugboardKey.unmatch();
                }
                plugboardKey.match(currentPlugboardKey, currentColor);
                currentPlugboardKey.match(plugboardKey, currentColor);
            }
            currentPlugboardKey = null;
        }
    }

    public void resetPlugboard() {
        this.initializeColors();

        currentPlugboardKey = null;

        if (allPlugboardKeys.isEmpty()) {
            for (int offset = 0; offset < Globals.ALPHABET_SIZE; offset++) {
                allPlugboardKeys.add(new PlugboardKey((char)('A' + offset)));
            }
        } else {
            allPlugboardKeys.forEach(plugboardKey -> {
                plugboardKey.unmatch();
            });
        }
    }

    public void configureRotor(int type) {
        int offset = 0;
        int ring = 0;

        switch (type) {
            case Globals.FAST_ROTOR:
                offset = rightRotorStart.getValue().charAt(0);
                ring = rightRotorRing.getValue().charAt(0);
                break;
            case Globals.MID_ROTOR:
                offset = middleRotorStart.getValue().charAt(0);
                ring = middleRotorRing.getValue().charAt(0);
                break;
            case Globals.SLOW_ROTOR:
                offset = leftRotorStart.getValue().charAt(0);
                ring = leftRotorRing.getValue().charAt(0);
                break;
        }

        machine.configureRotor(type, offset, ring);
    }

    public void configureRotors() {
        configureRotor(Globals.FAST_ROTOR);
        configureRotor(Globals.MID_ROTOR);
        configureRotor(Globals.SLOW_ROTOR);
    }

    public void configurePlugboard() {
        allPlugboardKeys.forEach((plugobardKey) -> {
            if (plugobardKey.isMatched()) {
                machine.setPlugboardPair(plugobardKey.getId(), plugobardKey.getPair().getId());
            }
        });
    };

    public List<PlugboardKey> getAllPlugboardKeys() {
        return allPlugboardKeys;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;

        this.configureRotors();
        this.configurePlugboard();
    }

    private void initializeColors() {
        currentColor = null;
        for (String color: colors) {
            freeColors.add(Paint.valueOf(color));
        }
    }
}
