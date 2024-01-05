package cz.vse.turistickaaplikace.controllers;

import cz.vse.turistickaaplikace.enumerators.AppChange;
import cz.vse.turistickaaplikace.interfaces.IObservable;
import cz.vse.turistickaaplikace.interfaces.IObserver;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cz.vse.turistickaaplikace.models.User;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LoginController implements Initializable, IObservable {
    private AppController appController;
    public void setAppController(AppController appController) {
        this.appController = appController;

    }

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Assuming you have a way to access userList from AppController
    private List<User> userList;

    public void setUserList(List<User> userList) {
        this.userList = userList;
    } //pro metodu authenticate


    @FXML
    private void handleLoginAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticate(username, password)) {
            // Successful login
            showSuccessAndRestoreOriginalView("Úspěšné přihlášení", "Vítejte, " + username + "!");
        } else {
            // Failed login
            showAlertDialog(Alert.AlertType.ERROR, "Neúspěch", "Špatné přihlašovací údaje.");
        }
    }
    @FXML
    private void handleCloseAction(ActionEvent event) {
        appController.restoreOriginalContentView();
    }


    private void showSuccessAndRestoreOriginalView(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();

        // Wait for 2 seconds and then restore the original view
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            alert.close();
            appController.restoreOriginalContentView();
        });
        pause.play();
    }


    private boolean authenticate(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void showAlertDialog(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void registruj(AppChange changeType, IObserver observer) {

    }
    // In your LoginController or RegistrationController

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

