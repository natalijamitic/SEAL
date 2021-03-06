package mn170085d;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mn170085d.gui.Controller;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../assets/gui.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Simple Enigma Machine Simulator");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../assets/icons/seal_icon.png")));

        Controller controller = loader.getController();
        controller.initializeApp();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
