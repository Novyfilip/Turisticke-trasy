package cz.vse.turistickaaplikace.components;

import cz.vse.turistickaaplikace.controllers.AppController;
import cz.vse.turistickaaplikace.models.Review;
import cz.vse.turistickaaplikace.models.Route;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReviewComponent extends VBox implements Initializable {

    private AppController appController;

    private Review review;

    @FXML
    public Label userLabel;

    @FXML
    public Label dateLabel;

    @FXML
    public HBox starsContainer;

    @FXML
    public Text commentLabel;

    public ReviewComponent() {
    }

    public ReviewComponent(Review review) {
        this.review = review;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/turistickaaplikace/views/review.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        colorStars();
        commentLabel.setText(review.getComment());
        dateLabel.setText(review.getDateTime());
        userLabel.setText(review.getAuthor());
    }

    public void colorStars() {
        Integer count = review.getReviewValue();

        for (int i = 0; i < starsContainer.getChildren().size(); i++) {
            if (count == 0)
                break;

            Node star = starsContainer.getChildren().get(i);
            star.setOpacity(1);
            count--;
        }
    }

    public void setAppController(AppController controller) {
        appController = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

