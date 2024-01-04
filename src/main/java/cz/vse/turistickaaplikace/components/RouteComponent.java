package cz.vse.turistickaaplikace.components;

import cz.vse.turistickaaplikace.controllers.AppController;
import cz.vse.turistickaaplikace.controllers.RouteDetailsController;
import cz.vse.turistickaaplikace.models.Route;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RouteComponent extends VBox implements Initializable {

    private AppController appController;

    private Route route;

    @FXML
    public Label titleLabel;

    @FXML
    public Label distanceLabel;

    @FXML
    public Label complexityLabel;

    @FXML
    public Circle complexityIcon;

    @FXML
    public Label reviewLabel;

    @FXML
    public Text descriptionLabel;

    public RouteComponent() {
    }

    public RouteComponent(Route route) {
        this(route, "route");
    }

    public RouteComponent(Route route, String viewName) {
        this.route = route;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/turistickaaplikace/views/"+ viewName +".fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        titleLabel.setText(route.getTitle());
        reviewLabel.setText(String.valueOf(route.getReviewMeanValue()));
        distanceLabel.setText(route.getDistance());
        complexityLabel.setText(route.getComplexity());
        complexityIcon.setFill(route.getComplexityColor());
        descriptionLabel.setText(route.getExcerpt());

        this.setOnMouseClicked(this::openRouteDetails);
    }

    public void setAppController(AppController controller) {
        appController = controller;
    }

    @FXML
    public void openRouteDetails(MouseEvent event) {
        appController.routeDetailsController.fillRoute(route);
        appController.openRouteDetailsPanel();
        drawRoute();
    }

    public void drawRoute() {
        appController.webEngine.executeScript("drawRoute("+ convertListToString(route.getPath()) +")");
    }

    private static String convertListToString(List<List<Double>> coordinatesList) {
        StringBuilder result = new StringBuilder("[");
        for (List<Double> coordinates : coordinatesList) {
            result.append("[")
                    .append(coordinates.get(0))
                    .append(", ")
                    .append(coordinates.get(1))
                    .append("], ");
        }
        // Remove the trailing comma and space
        if (coordinatesList.size() > 0) {
            result.setLength(result.length() - 2);
        }
        result.append("]");

        return result.toString();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

