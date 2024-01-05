package cz.vse.turistickaaplikace.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vse.turistickaaplikace.components.EmptyComponent;
import cz.vse.turistickaaplikace.components.ReviewComponent;
import cz.vse.turistickaaplikace.components.RouteComponent;
import cz.vse.turistickaaplikace.enumerators.AppChange;
import cz.vse.turistickaaplikace.interfaces.IObserver;
import cz.vse.turistickaaplikace.models.Review;
import cz.vse.turistickaaplikace.models.Route;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class RouteDetailsController implements Initializable {

    public Label titleLabel;

    public ScrollPane similarRoutesList;

    public ScrollPane reviewsList;

    public Label distanceLabel;

    public Label complexityLabel;

    public Circle complexityIcon;

    public Label reviewLabel;

    public Text descriptionLabel;

    private AppController appController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void fillRoute(Route route) {
        titleLabel.setText(route.getTitle());
        reviewLabel.setText(String.valueOf(route.getReviewMeanValue()));
        distanceLabel.setText(route.getDistance());
        complexityLabel.setText(route.getComplexity());
        complexityIcon.setFill(route.getComplexityColor());
        descriptionLabel.setText(route.getDescription());

        if (route.getSimilarRoutes().isEmpty()) {
            similarRoutesList.setContent(new EmptyComponent());
        } else {
            HBox elements = new HBox();
            for (Route similarRoute : route.getSimilarRoutes()) {
                RouteComponent component = new RouteComponent(similarRoute, "routeNarrow");
                component.setAppController(appController);
                elements.getChildren().add(component);
            }
            similarRoutesList.setContent(elements);
        }

        if (route.getReviews().isEmpty()) {
            reviewsList.setContent(new EmptyComponent());
        } else {
            VBox elements = new VBox();
            for (Review review : route.getReviews()) {
                ReviewComponent component = new ReviewComponent(review);
                component.setAppController(appController);
                elements.getChildren().add(component);
            }
            reviewsList.setContent(elements);
        }
    }

    public void setAppController(AppController controller) {
        appController = controller;
    }

    public void closeRouteDetailPanel(MouseEvent mouseEvent) {
        appController.closePanel();
        appController.webEngine.executeScript("clearMap()");
        appController.webEngine.executeScript("recenterMap()");

    }
}
