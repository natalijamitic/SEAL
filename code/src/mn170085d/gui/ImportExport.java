package mn170085d.gui;

import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mn170085d.Globals;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

public class ImportExport {
    private AnchorPane anchorPane;
    private TextArea inputText, outputText;

    public ImportExport(AnchorPane anchorPane, TextArea inputText, TextArea outputText) {
        this.anchorPane = anchorPane;
        this.inputText = inputText;
        this.outputText = outputText;
    }

    public void importText() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose the .txt file for import");
        Stage stage = (Stage) anchorPane.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        if (file == null){
            return;
        }

        String stringPath = file.getAbsolutePath();
        String regex = String.format("^.*\\%s$", Globals.FILE_EXTENSION);
        if (!stringPath.toLowerCase().matches(regex)) {
            return;
        }

        inputText.setText("");
        Path path = Paths.get(stringPath);
        try (Scanner scanner =  new Scanner(path, StandardCharsets.UTF_8.name())){
            while (scanner.hasNextLine()){
                inputText.appendText(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportText() {
        if (outputText.getText().length() == 0) {
            return;
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose location for export");
        Stage stage = (Stage)anchorPane.getScene().getWindow();

        File file = directoryChooser.showDialog(stage);
        if (file == null) {
            return;
        }

        String fileName= Globals.EXPORT_FILE_NAME + "_" + (new Date()).getTime() + Globals.FILE_EXTENSION;
        Path path = Paths.get(file.getAbsolutePath(), fileName);
        try {
            Path exportFilePath = Files.createFile(path);
            Files.write(exportFilePath, outputText.getText().getBytes());

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(file.getAbsolutePath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
