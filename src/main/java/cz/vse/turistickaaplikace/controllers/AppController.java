package cz.vse.turistickaaplikace.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML
    RoutesController routesController;

    @FXML
    FiltersController filtersController;

    @FXML
    public RouteDetailsController routeDetailsController;

    @FXML
    public VBox routes;

    @FXML
    public VBox filters;

    @FXML
    public VBox routeDetails;

    @FXML
    private WebView Map;

    public WebEngine webEngine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load map
        webEngine = Map.getEngine();
        webEngine.load(getClass().getResource("/cz/vse/turistickaaplikace/leaflet-maps.html").toExternalForm());

        routesController.setAppController(this);
        filtersController.setAppController(this);
        routeDetailsController.setAppController(this);
    }

    public void openFiltersPanel() {
        filters.setVisible(true);
        routes.setVisible(false);
    }

    public void openRouteDetailsPanel() {
        routeDetails.setVisible(true);
        routes.setVisible(false);
    }

    public void closePanel() {
        filters.setVisible(false);
        routeDetails.setVisible(false);
        routes.setVisible(true);
    }
}