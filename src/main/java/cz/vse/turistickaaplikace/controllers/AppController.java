package cz.vse.turistickaaplikace.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import cz.vse.turistickaaplikace.components.RouteComponent;
import cz.vse.turistickaaplikace.components.UsersComponent;
import cz.vse.turistickaaplikace.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.File;


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
    private HashMap<String, User> userMap = new HashMap<>();
    private final String userFilePath = "src/main/resources/cz/vse/turistickaaplikace/users.json";

    private UsersComponent usersComponent = new UsersComponent();

    private User loggedUser;

    public HashMap<String, User> getUserMap() {
        return userMap;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load map
        resetView();
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

    public UsersComponent getUsersComponent() {
        return usersComponent;
    }

    private void loadView(String fxmlPath, VBox container) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            container.getChildren().clear();
            container.getChildren().add(view);

            Object controller = loader.getController();
            if (controller instanceof LoginController) {
                ((LoginController) controller).setAppController(this);
                ((LoginController) controller).setUserMap(getUserMap());

        } else if (controller instanceof RegistrationController) {
                ((RegistrationController) controller).setAppController(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void handleLogout(ActionEvent actionEvent) {
        isLoggedIn = false;
        setLoggedInUser(null);
        resetView();
        loggedInUserLabel.setText("Odhlášeno");
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
    //Přihlášený uživatel
    @FXML
    private Label loggedInUserLabel;
    @FXML
    private MenuItem homeMenuItem;
    @FXML
    private Button registerButton;

    public void setLoggedInUser(User user) {
        if (user != null) {
            loggedInUserLabel.setText(user.getUsername());
            loggedUser = user;
            registerButton.setVisible(false); // Hide Register button
        } else {
            loggedInUserLabel.setText("Nepřihlášeno");
            registerButton.setVisible(true); // Show Register button
        }
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    @FXML
    private void handleHomeAction(ActionEvent event) {
        resetView();
    }
    public void resetView() {
        // Load map and set controllers
        webEngine = Map.getEngine();
        webEngine.load(getClass().getResource("/cz/vse/turistickaaplikace/leaflet-maps.html").toExternalForm());

        routesController.setAppController(this);
        filtersController.setAppController(this);
        routeDetailsController.setAppController(this);

        // Additional steps to reset the view, if any
    }






}


