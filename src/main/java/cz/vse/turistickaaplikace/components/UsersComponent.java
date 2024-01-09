package cz.vse.turistickaaplikace.components;

import cz.vse.turistickaaplikace.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersComponent {
    public DatabaseComponent db = new DatabaseComponent();

    public User loggedInUser;

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public User loginUser(String username, String password) {
        String loginQuerry = "SELECT * FROM User WHERE username LIKE " + "'" + username + "'";
        try (Connection connection = db.connect();
             PreparedStatement pstmt = connection.prepareStatement(loginQuerry)) {
            ResultSet user = pstmt.executeQuery();
            while (user.next()) {
                if (user.getString(5).equals(password)) {
                    String name = user.getString(1);
                    String surname = user.getString(2);
                    String email = user.getString(3);
                    User loggedUser = new User(username, password, email, name, surname);
                    this.loggedInUser = loggedUser;
                    return loggedUser;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Boolean registerUser(String name, String surname, String email, String username, String password) {
        String checkUsername = "SELECT * FROM User WHERE username LIKE " + "'" + username + "'";
        try (Connection connection = db.connect();
             PreparedStatement pstmt = connection.prepareStatement(checkUsername)) {
            ResultSet user = pstmt.executeQuery();
            while (user.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String insertUserQuery = "INSERT INTO User (jmeno, prijmeni, email, username, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = db.connect();
             PreparedStatement pstmt = connection.prepareStatement(insertUserQuery)) {
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setString(3, email);
            pstmt.setString(4, username);
            pstmt.setString(5, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
