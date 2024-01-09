package cz.vse.turistickaaplikace.components;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseComponent {

    public Connection connect() {
        String DBurl = "jdbc:sqlite:src/main/resources/cz/vse/turistickaaplikace/data/horyDB.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBurl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }


}
