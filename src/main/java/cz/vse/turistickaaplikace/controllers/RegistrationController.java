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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import cz.vse.turistickaaplikace.models.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    private Map<String, User> userMap;

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }


    @FXML
    private void handleRegistrationAction(ActionEvent event) {
        String jmeno = jmenoField.getText();
        String prijmeni = prijmeniField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String heslo = hesloField.getText();

        // Registrační metoda s validací
        if (registerUser(username, email, heslo, jmeno, prijmeni)) {
            showAlertDialog(Alert.AlertType.INFORMATION, "Registrace Dokončena", "Váš účet byl úspěšně vytvořen. Nyní se můžete přihlásit.");
        } else {
            // Show error message if validation fails
            showAlertDialog(Alert.AlertType.ERROR, "Registrace selhala", "Vaše údaje nesplňují požadavky. Vyplňte povinné údaje s heslem dlouhým alespoň 8 znaků.");
        }
    }
    private boolean isUsernameUnique(String username) {
        return !appController.getUserMap().containsKey(username);
    }


    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }
    private boolean registerUser(String username, String email, String password, String jmeno, String prijmeni) {
        if (isUsernameUnique(username) && isEmailValid(email) && isPasswordValid(password)) {
            String hashedPassword = hashWithSHA(password);
            String hashedEmail = hashWithSHA(email);
            // Hashujeme osobní udaje
            String hashedJmeno = hashWithSHA(jmeno);
            String hashedPrijmeni = hashWithSHA(prijmeni);

            //User newUser = new User(username, hashedPassword, hashedEmail, hashedJmeno, hashedPrijmeni, false, null);
            //appController.getUserMap().put(username, newUser); // Přidá se do dictionary
            //appController.saveUsers(); // Poté se uloží do souboru
            return true;
        }
        return false;
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
}
