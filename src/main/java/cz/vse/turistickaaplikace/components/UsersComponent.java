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

    public Boolean loginUser(String username, String password) {
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
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
