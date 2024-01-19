package cz.vse.turistickaaplikace.controllers;

import cz.vse.turistickaaplikace.components.UsersComponent;
import cz.vse.turistickaaplikace.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;


public class AppController implements Initializable {
    @FXML
    RoutesController routesController;

    @FXML
    FiltersController filtersController;

    @FXML
    LoginController loginController;

    @FXML
    RegistrationController registrationController;

    @FXML
    public RouteDetailsController routeDetailsController;

    @FXML
    public VBox routes;

    @FXML
    public VBox filters;

    @FXML
    public VBox routeDetails;

    @FXML
    public GridPane login, registration;

    private Pane lastOpenedPanel;

    @FXML
    private WebView Map;

    public WebEngine webEngine;
    private boolean isLoggedIn = false;
    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }
    private HashMap<String, User> userMap = new HashMap<>();
    //původní soubor pro ukládání uživatelů, nyní v databázi
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

    public void openLastOpenedPanel() {
        closeAllPanels();

        if (lastOpenedPanel != null) {
            lastOpenedPanel.setVisible(true);
        } else {
            openRoutesPanel();
        }
    }

    public void openFiltersPanel() {
        closeAllPanels();
        filters.setVisible(true);
        lastOpenedPanel = filters;
    }

    public void openRouteDetailsPanel() {
        closeAllPanels();
        routeDetails.setVisible(true);
        lastOpenedPanel = routeDetails;
    }

    public void openRoutesPanel() {
        closeAllPanels();
        routes.setVisible(true);
        lastOpenedPanel = routes;
    }

    public void closeAllPanels() {
        filters.setVisible(false);
        routeDetails.setVisible(false);
        routes.setVisible(false);
        login.setVisible(false);
        registration.setVisible(false);
    }

    public void handleLogin(ActionEvent actionEvent) {
        closeAllPanels();
        login.setVisible(true);
    }

    public void handleRegister(ActionEvent actionEvent) {
        closeAllPanels();
        registration.setVisible(true);
    }

    public void updateAuthenticatedUi() {
        routeDetailsController.refreshCommentField();
    }

    public UsersComponent getUsersComponent() {
        return usersComponent;
    }

    public void handleLogout(ActionEvent actionEvent) {
        isLoggedIn = false;
        setLoggedInUser(null);
        loggedInUserLabel.setText("Odhlášeno");
        openLastOpenedPanel();
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
    private Button registerButton, loginButton, logoutButton;

    public void setLoggedInUser(User user) {
        if (user != null) {
            loggedInUserLabel.setText(user.getUsername());
            loggedInUserLabel.setManaged(true);
            loggedInUserLabel.setVisible(true);
            loggedUser = user;
            registerButton.setVisible(false);
            loginButton.setVisible(false);
            logoutButton.setManaged(true);
            logoutButton.setVisible(true);
        } else {
            loggedInUserLabel.setText("Nepřihlášeno");
            loggedInUserLabel.setManaged(false);
            loggedInUserLabel.setVisible(false);
            registerButton.setVisible(true);
            loginButton.setVisible(true);
            logoutButton.setManaged(false);
            logoutButton.setVisible(false);
        }
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void resetView() {
        webEngine = Map.getEngine();
        webEngine.load(getClass().getResource("/cz/vse/turistickaaplikace/leaflet-maps.html").toExternalForm());

        routesController.setAppController(this);
        filtersController.setAppController(this);
        routeDetailsController.setAppController(this);
        loginController.setAppController(this);
        registrationController.setAppController(this);

    }
}