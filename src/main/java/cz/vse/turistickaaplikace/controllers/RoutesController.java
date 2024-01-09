package cz.vse.turistickaaplikace.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vse.turistickaaplikace.components.DatabaseComponent;
import cz.vse.turistickaaplikace.components.EmptyComponent;
import cz.vse.turistickaaplikace.components.RouteComponent;
import cz.vse.turistickaaplikace.enumerators.AppChange;
import cz.vse.turistickaaplikace.interfaces.IObserver;
import cz.vse.turistickaaplikace.models.Route;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class RoutesController implements Initializable, IObserver {

    @FXML
    private ScrollPane routeListContainer;

    private AppController appController;

    private List<Route> routeList = new ArrayList<>();

    private List<Route> filteredRouteList = new ArrayList<>();

    private DatabaseComponent db = new DatabaseComponent();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadRoutes();
    }

    private void loadRoutes() {
        String loadRoutesQuerry = "SELECT * FROM Route";
        try (Connection connection = db.connect();
             PreparedStatement pstmt = connection.prepareStatement(loadRoutesQuerry)) {
             ResultSet routesSet = pstmt.executeQuery();
             while (routesSet.next()) {
                 Route route = new Route();
                 route.setId(routesSet.getInt(1));
                 route.setTitle(routesSet.getString(2));
                 route.setDistance(routesSet.getInt(3));
                 route.setTimeToComplete(routesSet.getInt(4));
                 route.setComplexity(routesSet.getInt(5));
                 route.setDescription(routesSet.getString(6));
                 routeList.add(route);
             }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayRoutes() {
        filterRoutes();

        if (filteredRouteList.isEmpty()) {
            routeListContainer.setContent(new EmptyComponent("Podle zadaných filtrů nejsou žádné trasy."));
            return;
        }

        VBox elements = new VBox();
        for (Route route : filteredRouteList) {
            RouteComponent routeComponent = new RouteComponent(route);
            routeComponent.setAppController(appController);
            elements.getChildren().add(routeComponent);
        }
        routeListContainer.setContent(elements);
    }

    private void filterRoutes() {
        if (appController == null) {
            filteredRouteList = routeList;
            return;
        }

        Double minDistance = appController.filtersController.getMinDistanceValue();
        Double maxDistance = appController.filtersController.getMaxDistanceValue();
        Double review = appController.filtersController.getReviewValue();
        Set complexities = appController.filtersController.getComplexities();

        filteredRouteList = routeList.stream()
                .filter(route ->
                        route.getDistanceValue() >= minDistance &&
                        route.getDistanceValue() <= maxDistance &&
                        route.getReviewMeanValue() >= review &&
                        complexities.contains(route.getComplexityValue()))
                .collect(Collectors.toList());
    }

    public void setAppController(AppController controller) {
        appController = controller;
        setObservers();
        displayRoutes();
    }

    public void setObservers() {
        appController.filtersController.registruj(AppChange.FILTERS_CHANGE, () -> {
            displayRoutes();
        });
    }

    public void openFiltersPanel(MouseEvent mouseEvent) {
        appController.openFiltersPanel();
    }

    @Override
    public void aktualizuj() {}
}
