package cz.vse.turistickaaplikace.components;

import cz.vse.turistickaaplikace.models.Route;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class EmptyComponent extends FlowPane {

    @FXML
    public Text textLabel;

    public EmptyComponent() {
        this("Žádné");
    }

    public EmptyComponent(String s) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/turistickaaplikace/views/empty.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        textLabel.setText(s);
    }
}
