package mn170085d.gui;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import mn170085d.Globals;
import mn170085d.enigma.Machine;

public class Controller {
    @FXML
    Pane textboxPane, keyboardPane, simulationPane, settingsPane, rotorStatePane, drawingPane, loadingPane;
    @FXML
    ComboBox<String> reflectorTypes, leftRotorTypes, middleRotorTypes, rightRotorTypes, leftRotorStart;
    @FXML
    ComboBox<String> middleRotorStart, rightRotorStart, leftRotorRing, middleRotorRing, rightRotorRing;
    @FXML
    Label leftRotorState, rightRotorState, middleRotorState, inputSimulationLabel, outputSimulationLabel;
    @FXML
    Label plugboardLeft, plugboardRight, leftRotorLeft, leftRotorRight, middleRotorLeft, middleRotorRight;
    @FXML
    Label rightRotorLeft, rightRotorRight, reflectorRight, rightRotorLabel, middleRotorLabel, leftRotorLabel, keyboardInputLabel, keyboardOutputLabel;
    @FXML
    TextArea inputText, outputText, inputTextSimulation, outputTextSimulation;
    @FXML
    Menu settingsMenu, modeMenu, resetToDefaultMenu;
    @FXML
    AnchorPane anchorPane;
    @FXML
    Rectangle leftRotorRectangle, rightRotorRectangle, middleRotorRectangle;
    @FXML
    ImageView sealImg, enigmaImg;
    @FXML
    ToggleGroup displayMode;
    @FXML
    RadioMenuItem textBoxMenuItem;
    @FXML
    StackPane delStackPane;

    private boolean isSettingsPaneInitialized = false, isKeyboardModeActive = false;

    /************************************************
     *                  MACHINE                     *
     ************************************************/
    private Machine machine;

    public void createNewMachine() {
        machine = new Machine(rightRotorTypes.getValue(), middleRotorTypes.getValue(), leftRotorTypes.getValue(), reflectorTypes.getValue(), rightRotorRing.getValue(), rightRotorStart.getValue(), middleRotorRing.getValue(), middleRotorStart.getValue(), leftRotorRing.getValue(), leftRotorStart.getValue());
        if (settings != null) {
            settings.setMachine(machine);
        }
        updateMachine();
    }

    public void updateMachine() {
        if (simulation != null) {
            simulation.setMachine(machine);
        }
        if (keyboard != null) {
            keyboard.setMachine(machine);
        }
        updateRotorStates(true);
        hideSimulationLabels();
    }

    public void onRotorUp(MouseEvent event){
        machine.decRotorOffset(Character.getNumericValue(((Circle)event.getSource()).getId().charAt(5)));
        updateMachine();
    }
    public void onRotorDown(MouseEvent event){
        machine.incRotorOffset(Character.getNumericValue(((Circle)event.getSource()).getId().charAt(5)));
        updateMachine();
    }

    private void updateRotorStates(boolean displayLabels) {
        leftRotorLabel.setVisible(displayLabels && !isKeyboardModeActive);
        middleRotorLabel.setVisible(displayLabels && !isKeyboardModeActive);
        rightRotorLabel.setVisible(displayLabels && !isKeyboardModeActive);
        leftRotorRectangle.setFill(displayLabels && !isKeyboardModeActive ? Globals.ROTOR_STATE_DEFAULT_COLOR : Globals.ROTOR_STATE_DARK_COLOR);
        middleRotorRectangle.setFill(displayLabels && !isKeyboardModeActive ? Globals.ROTOR_STATE_DEFAULT_COLOR : Globals.ROTOR_STATE_DARK_COLOR);
        rightRotorRectangle.setFill(displayLabels && !isKeyboardModeActive ? Globals.ROTOR_STATE_DEFAULT_COLOR : Globals.ROTOR_STATE_DARK_COLOR);
        leftRotorState.setText(Character.toString((char)(machine.getRotor(Globals.SLOW_ROTOR).getOffset() + Globals.A_CODE)));
        middleRotorState.setText(Character.toString((char)(machine.getRotor(Globals.MID_ROTOR).getOffset() + Globals.A_CODE)));
        rightRotorState.setText(Character.toString((char)(machine.getRotor(Globals.FAST_ROTOR).getOffset() + Globals.A_CODE)));
    }


    /************************************************
     *                  HANDLE PANES                *
     ************************************************/
    public void openTextboxPane() {
        removeDisplayingClass(settingsMenu);
        leaveSimulationMode();
        leaveKeyboardMode();
        textboxPane.toFront();
        textBoxMenuItem.setSelected(true);
        bringRotorStatesToFront(true);
    }
    public void openKeyboardPane() {
        removeDisplayingClass(settingsMenu);
        leaveSimulationMode();

        if (keyboard == null) {
            createNewKeyboard();
        }

        isKeyboardModeActive = true;
        keyboardPane.toFront();
        bringRotorStatesToFront(false);
    }
    public void openSimulationPane() {
        leaveKeyboardMode();
        removeDisplayingClass(settingsMenu);
        inputTextSimulation.setDisable(false);
        if (simulation == null) {
            createNewSimulation();
        }

        simulationPane.toFront();
        drawingPane.toFront();
        bringRotorStatesToFront(true);

        hideSimulationLabels();

        refreshWires();
    }
    public void openSettingsPane() {
        leaveKeyboardMode();
        leaveSimulationMode();
        if (!isSettingsPaneInitialized) {
            initializeSettingsPane();
            isSettingsPaneInitialized = true;
        }
        setDisplayingClass(settingsMenu);
        settingsPane.toFront();
    }

    private void bringRotorStatesToFront(boolean displayLabels) {
        rotorStatePane.toFront();
        updateRotorStates(displayLabels);
    }

    private void setDisplayingClass(Menu menuToSet) {
        if (!menuToSet.getStyleClass().contains(Globals.CSS_CLASS_ON_DISPLAY)) {
            menuToSet.getStyleClass().add(Globals.CSS_CLASS_ON_DISPLAY);
        }
    }

    private void removeDisplayingClass(Menu menuToRemove) {
        menuToRemove.getStyleClass().remove(Globals.CSS_CLASS_ON_DISPLAY);
    }

    private void hideSimulationLabels() {
        inputSimulationLabel.setVisible(false);
        outputSimulationLabel.setVisible(false);
    }

    private void leaveSimulationMode() {
        inputTextSimulation.setDisable(true);
    }

    private void leaveKeyboardMode() {
        isKeyboardModeActive = false;
    }
    /************************************************
     *                  SETTINGS PANE               *
     ************************************************/
    private Settings settings;

    private void createNewSettings() {
        settings = new Settings(machine, reflectorTypes, leftRotorTypes, middleRotorTypes, rightRotorTypes, leftRotorStart, middleRotorStart, rightRotorStart, leftRotorRing, middleRotorRing, rightRotorRing);
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
        updateRotorStates(true);
    }

    /************************************************
     *                  TEXT BOX                    *
     ************************************************/
    public void encryptText() {
        String text = inputText.getText();
        outputText.setText(machine.cryptText(text));
        updateRotorStates(true);
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
        openTextboxPane();
        importExport.importText();
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
     *                 KEYBOARD MODE                *
     ************************************************/
    private Keyboard keyboard;

    private void createNewKeyboard() {
        keyboard = new Keyboard(machine, keyboardInputLabel, keyboardOutputLabel, delStackPane);
    }

    public void keyboardSelectKey(Event e) {
        keyboard.selectKey((StackPane)e.getSource());
        updateRotorStates(false);

    }

    public void keyboardDelete() {
        keyboard.deleteLabels();
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
        fireKeyEventsKeyboardMode();

        createImportExport();
        loadingAnimation();
    }

    private int counter = 0;

    private void loadingAnimation() {
        loadingPane.setVisible(true);
        loadingPane.toFront();
        double distance = Math.abs(sealImg.localToScreen(sealImg.getBoundsInLocal()).getMinX() - enigmaImg.localToScreen(enigmaImg.getBoundsInLocal()).getMinX());
        double deltaDistance = 4000 / distance;
        double rotation = sealImg.getRotate();

        AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now) {
                if (sealImg.getBoundsInParent().intersects(enigmaImg.getBoundsInParent())) {
                    this.stop();
                    loadingPane.setVisible(false);
                    openTextboxPane();
                    return;
                }
                if (counter % 15 == 0) {
                    sealImg.setRotate(rotation - 2);
                } else if (counter % 10 == 0) {
                    sealImg.setRotate(rotation);
                } else if (counter % 5 == 0) {
                    sealImg.setRotate(rotation + 2);
                }
                sealImg.setTranslateX(deltaDistance * (counter++));
            }
        };
        timer.start();
    }

    private void fireKeyEventsKeyboardMode() {
        Scene scene = keyboardPane.getScene();
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (isKeyboardModeActive) {
                if (event.getCode() == KeyCode.BACK_SPACE) {
                    keyboardDelete();
                }
                if (event.isShortcutDown() || !event.getCode().isLetterKey() || event.getText().length() == 0) {
                    event.consume();
                } else {
                    keyboard.pressedKey(event, scene);
                    updateRotorStates(false);
                }
            }
        });
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
