package mn170085d.gui;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mn170085d.Globals;
import mn170085d.enigma.Machine;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

import java.nio.charset.StandardCharsets;


public class Controller {
    @FXML
    Pane textboxPane, keyboardPane, simulationPane, settingsPane, rotorStatePane, drawingPane;
    @FXML
    ChoiceBox<String> reflectorTypes, leftRotorTypes, middleRotorTypes, rightRotorTypes, leftRotorStart;
    @FXML
    ChoiceBox<String> middleRotorStart, rightRotorStart, leftRotorRing, middleRotorRing, rightRotorRing;
    @FXML
    Label leftRotorState, rightRotorState, middleRotorState, inputSimulationLabel, outputSimulationLabel;
    @FXML
    Label plugboardLeft, plugboardRight, leftRotorLeft, leftRotorRight, middleRotorLeft, middleRotorRight;
    @FXML
    Label rightRotorLeft, rightRotorRight, reflectorRight;
    @FXML
    TextArea inputText, outputText, inputTextSimulation, outputTextSimulation;
    @FXML
    Menu settingsMenu, modeMenu, resetToDefaultMenu;
    @FXML
    AnchorPane anchorPane;

    private Boolean isSettingsPaneInitialized = false;

    /************************************************
     *                  MACHINE                     *
     ************************************************/
    private Machine machine;

    public void createNewMachine() {
        machine = new Machine(rightRotorTypes.getValue(), middleRotorTypes.getValue(), leftRotorTypes.getValue(), reflectorTypes.getValue(), rightRotorRing.getValue(), rightRotorStart.getValue(), middleRotorRing.getValue(), middleRotorStart.getValue(), leftRotorRing.getValue(), leftRotorStart.getValue());
        if (settings != null) {
            settings.setMachine(machine);
        }
        if (simulation != null) {
            simulation.setMachine(machine);
        }
        updateRotorStates();
        hideSimulationLabels();
    }

    private void updateRotorStates() {
        this.leftRotorState.setText(Character.toString((char)(machine.getRotor(Globals.SLOW_ROTOR).getOffset() + Globals.A_CODE)));
        this.middleRotorState.setText(Character.toString((char)(machine.getRotor(Globals.MID_ROTOR).getOffset() + Globals.A_CODE)));
        this.rightRotorState.setText(Character.toString((char)(machine.getRotor(Globals.FAST_ROTOR).getOffset() + Globals.A_CODE)));
    }


    /************************************************
     *                  HANDLE PANES                *
     ************************************************/
    public void openTextboxPane() {
        setDisplayingClass(modeMenu, settingsMenu);
        leaveSimulationMode();
        textboxPane.toFront();
        bringRotorStatesToFront();
    }
    public void openKeyboardPane() {
        setDisplayingClass(modeMenu, settingsMenu);
        leaveSimulationMode();
        keyboardPane.toFront();
        bringRotorStatesToFront();
    }
    public void openSimulationPane() {
        setDisplayingClass(modeMenu, settingsMenu);
        inputTextSimulation.setDisable(false);
        if (simulation == null) {
            createNewSimulation();
        }

        simulationPane.toFront();
        drawingPane.toFront();
        bringRotorStatesToFront();

        hideSimulationLabels();

        refreshWires();
    }
    public void openSettingsPane() {
        leaveSimulationMode();
        if (!isSettingsPaneInitialized) {
            initializeSettingsPane();
            isSettingsPaneInitialized = true;
        }
        setDisplayingClass(settingsMenu, modeMenu);
        settingsPane.toFront();
    }

    private void bringRotorStatesToFront() {
        rotorStatePane.toFront();
        updateRotorStates();
    }

    private void setDisplayingClass(Menu menuToSet, Menu menuToRemove) {
        if (!menuToSet.getStyleClass().contains(Globals.CSS_CLASS_ON_DISPLAY)) {
            menuToSet.getStyleClass().add(Globals.CSS_CLASS_ON_DISPLAY);
        }
        menuToRemove.getStyleClass().remove(Globals.CSS_CLASS_ON_DISPLAY);
    }

    private void hideSimulationLabels() {
        inputSimulationLabel.setVisible(false);
        outputSimulationLabel.setVisible(false);
    }

    private void leaveSimulationMode() {
        inputTextSimulation.setDisable(true);
    }

    /************************************************
     *                  SETTINGS PANE               *
     ************************************************/
    private Settings settings;

    private void createNewSettings() {
        settings = new Settings(machine, reflectorTypes,leftRotorTypes, middleRotorTypes, rightRotorTypes, leftRotorStart, middleRotorStart, rightRotorStart, leftRotorRing, middleRotorRing, rightRotorRing);
    }

    private void initializeSettingsPane() {
        if (settings == null) {
            createNewSettings();
        }

        resetPlugboard();
        settings.resetRotorsAndReflector();

        if (this.machine == null) {
            createNewMachine();
        }
    }

    public void selectPlugboardKey(Event e) {
        settings.selectPlugboardKey(e);
    }

    public void resetPlugboard() {
        settings.resetPlugboard();
    }

    public void saveSettings() {
        createNewMachine();
    }

    public void resetToDefault() {
        createNewMachine();
    }

    /************************************************
     *                  SIMULATION                  *
     ************************************************/
    private Simulation simulation;

    private void createNewSimulation() {
        LineDrawer lineDrawer = new LineDrawer(plugboardLeft, plugboardRight, leftRotorLeft ,leftRotorRight, middleRotorLeft, middleRotorRight, rightRotorLeft, rightRotorRight, reflectorRight, drawingPane, inputSimulationLabel, outputSimulationLabel);
        simulation = new Simulation(machine, lineDrawer);
    }

    private void refreshWires() {
        simulation.refreshWires();
    }

    public void simulationPressedKey(KeyEvent e) {
        KeyCode pressedKeyCode = e.getCode();

        if (!pressedKeyCode.isLetterKey() || e.isShortcutDown()) {
            String outputText = outputTextSimulation.getText();
            if (e.getCode() == KeyCode.BACK_SPACE && outputText.length() > 0) {
                outputTextSimulation.setText(outputText.substring(0, outputText.length() - 1));
            } else {
                e.consume();
            }
            return;
        }

        char output = simulation.simulateCryption(pressedKeyCode.getName().charAt(0));
        outputTextSimulation.appendText(Character.toString(output));
        updateRotorStates();
    }

    /************************************************
     *                  TEXT BOX                    *
     ************************************************/
    public void encryptText() {
        String text = this.inputText.getText();
        this.outputText.setText(machine.cryptText(text));
        this.updateRotorStates();
    }

    public void removeText() {
        this.outputText.setText("");
        this.inputText.setText("");
    }

    /************************************************
     *                  IMPORT EXPORT              *
     ************************************************/
    private ImportExport importExport;

    public void importText() {
        importExport.importText();
        openTextboxPane();
    }

    public void exportText() {
        importExport.exportText();
    }

    private void createImportExport() {
        if (importExport == null) {
            importExport = new ImportExport(anchorPane, inputText, outputText);
        }
    }

    public void quitApp() {
        Platform.exit();
    }

    /************************************************
     *              APP INITIALIZATION              *
     ************************************************/

    public void initializeApp() {
        initializeSettingsPane();
        setUppercaseSimulationInput();
        disableShortcutsOnInputField(inputTextSimulation);
        disableShortcutsOnInputField(inputText);
        disableAltShortcuts();
        fireSettingsMenuEvent();
        fireResetToDefaultMenuEvent();

        createImportExport();

        openTextboxPane();
    }

    private void setUppercaseSimulationInput(){
        inputTextSimulation.textProperty().addListener(
                (observer, oldValue, newValue) -> {
                    if (!newValue.matches("[a-zA-Z]*")) {
                        inputTextSimulation.setText(newValue.replaceAll("[^a-zA-Z]", ""));
                    } else {
                        inputTextSimulation.setText(newValue.toUpperCase());
                    }
                }
        );
    }

    private void disableShortcutsOnInputField(TextArea input) {
        input.addEventFilter(KeyEvent.ANY, e -> {
            if ((e.getCode() == KeyCode.Z || e.getCode() == KeyCode.Y)&& e.isShortcutDown()) {
                e.consume();
            }
        });
    }

    private void disableAltShortcuts() {
        anchorPane.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.isAltDown() || KeyCode.ALT_GRAPH == e.getCode()) {
                e.consume();
            }
        });
    }

    private void fireSettingsMenuEvent() {
        settingsMenu.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                settingsMenu.getItems().get(0).fire();
            }
        });
    }

    private void fireResetToDefaultMenuEvent() {
        resetToDefaultMenu.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                resetToDefaultMenu.getItems().get(0).fire();
            }
        });
    }
}
