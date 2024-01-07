package cz.vse.turistickaaplikace.controllers;

import cz.vse.turistickaaplikace.enumerators.AppChange;
import cz.vse.turistickaaplikace.interfaces.IObservable;
import cz.vse.turistickaaplikace.interfaces.IObserver;
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

public class RegistrationController implements Initializable, IObservable {
    private AppController appController;
    public void setAppController(AppController appController) {
        this.appController = appController;

    }

    @FXML
    private TextField jmenoField;

    @FXML
    private TextField prijmeniField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField hesloField;

    private List<User> userList;

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @FXML
    private void handleRegistrationAction(ActionEvent event) {
        String jmeno = jmenoField.getText();
        String prijmeni = prijmeniField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String heslo = hesloField.getText();

        User newUser = new User(username, heslo, email, jmeno, prijmeni, false, null);
        userList.add(newUser);
        showAlertDialog(Alert.AlertType.INFORMATION, "Registration Successful", "User " + username + " has been registered.");
    }
    @FXML
    private void handleCloseAction(ActionEvent event) {
        appController.restoreOriginalContentView();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
