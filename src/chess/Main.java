package chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static int height;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("gui/chess.fxml"));
        primaryStage.setTitle("John's Chess");
        primaryStage.getIcons().add(new Image("images/icon.png"));
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.setMaximized(true);
        primaryStage.show();
        height = (int) primaryStage.getHeight() - 30;

    }


    public static void main(String[] args) {
        launch(args);
    }
}
