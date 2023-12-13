package cz.vse.turistickaaplikace.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadRoutes();
    }

    private void loadRoutes() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/cz/vse/turistickaaplikace/routes.json");

            ObjectMapper routeMapper = new ObjectMapper();

            if (inputStream != null) {
                routeList = routeMapper.readValue(inputStream, new TypeReference<List<Route>>() {});
            } else {
                throw new FileNotFoundException("Resource not found: routes.json");
            }
        } catch (IOException e) {
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
