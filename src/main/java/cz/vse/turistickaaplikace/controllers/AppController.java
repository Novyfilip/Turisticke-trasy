package cz.vse.turistickaaplikace.controllers;

import cz.vse.turistickaaplikace.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    private boolean isLoggedIn = false;
    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }
    private List<User> userList = new ArrayList<>();

    public List<User> getUserList() {
        return userList;
    }

    public void loadUsers() {
        // Mělo by načítat z databáze, dodělat
    }




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
    public void handleLogin(ActionEvent actionEvent) {
        storeOriginalContentView(); // Store the current view
        loadView("/cz/vse/turistickaaplikace/views/login.fxml", routes);
    }

    public void handleRegister(ActionEvent actionEvent) {
        storeOriginalContentView(); // Store the current view
        loadView("/cz/vse/turistickaaplikace/views/registration.fxml", routes);
    }



    private void loadView(String fxmlPath, VBox container) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            container.getChildren().clear(); // Clear current view
            container.getChildren().add(view); // Add new view

            Object controller = loader.getController();
            if (controller instanceof LoginController) {
                ((LoginController) controller).setAppController(this);
                ((LoginController) controller).setUserList(getUserList());

        } else if (controller instanceof RegistrationController) {
                ((RegistrationController) controller).setAppController(this);
            }

            // Add similar conditions for other controllers if necessary
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }



    public void handleLogout(ActionEvent actionEvent) {
        isLoggedIn = false;
        loadView("/cz/vse/turistickaaplikace/views/routes.fxml", routes);
    }




    private Stage getStage() {
        return (Stage) Map.getScene().getWindow();
    }
    private List<Node> originalContentView;

    public void storeOriginalContentView() {
        this.originalContentView = new ArrayList<>(routes.getChildren());
    }

    public void restoreOriginalContentView() {
        if (originalContentView != null) {
            routes.getChildren().setAll(originalContentView);
        }
    }

}


