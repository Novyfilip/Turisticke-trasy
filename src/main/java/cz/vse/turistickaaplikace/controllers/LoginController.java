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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import cz.vse.turistickaaplikace.models.User;
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
    private Map<String, User> userMap;

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }
    //pro metodu authenticate


    @FXML
    private void handleLoginAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Boolean result = appController.getUsersComponent().loginUser(username,password);
        if (result) {
            // Successful login
            appController.setLoggedInUser(username);
            appController.setLoggedUser(appController.getUsersComponent().getLoggedInUser());
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
        String hashedPassword = hashWithSHA(password);
        System.out.println("Hešované heslo: " + hashedPassword); // debugging

        User user = userMap.get(username);
        if (user != null && user.getPassword().equals(hashedPassword)) {
            System.out.println("Očekáváno: " + user.getPassword()); // debugging
            return true;
        }
        return false;
    }


    public String hashWithSHA(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // možná předělám
        }
    }
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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

