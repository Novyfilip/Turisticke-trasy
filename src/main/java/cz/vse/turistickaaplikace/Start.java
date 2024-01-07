package cz.vse.turistickaaplikace;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Start extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/app.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(getClass().getResource("images/hiking.png").toExternalForm()));
        stage.setTitle("Turistická aplikace");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
