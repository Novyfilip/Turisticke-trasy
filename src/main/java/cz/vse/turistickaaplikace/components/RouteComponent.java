package cz.vse.turistickaaplikace.components;

import cz.vse.turistickaaplikace.controllers.AppController;
import cz.vse.turistickaaplikace.controllers.RouteDetailsController;
import cz.vse.turistickaaplikace.models.Review;
import cz.vse.turistickaaplikace.models.Route;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RouteComponent extends VBox implements Initializable {

    private AppController appController;

    private Route route;

    @FXML
    public Label titleLabel;

    @FXML
    public Label distanceLabel;

    @FXML
    public Label complexityLabel;

    @FXML
    public Circle complexityIcon;

    @FXML
    public Label reviewLabel;

    @FXML
    public Text descriptionLabel;

    private DatabaseComponent db = new DatabaseComponent();

    public RouteComponent() {
    }

    public RouteComponent(Route route) {
        this(route, "route");
    }

    public RouteComponent(Route route, String viewName) {
        this.route = route;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vse/turistickaaplikace/views/"+ viewName +".fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        titleLabel.setText(route.getTitle());
        reviewLabel.setText(String.valueOf(route.getReviewMeanValue()));
        distanceLabel.setText(route.getDistance());
        complexityLabel.setText(route.getComplexity());
        complexityIcon.setFill(route.getComplexityColor());
        descriptionLabel.setText(route.getExcerpt());

        this.setOnMouseClicked(this::openRouteDetails);
    }

    public void setAppController(AppController controller) {
        appController = controller;
    }

    @FXML
    public void openRouteDetails(MouseEvent event) {
        loadReviews();
        //drawRoute();
        appController.routeDetailsController.setRouteComponent(this);
        appController.routeDetailsController.fillRoute(route);
        appController.openRouteDetailsPanel();
    }

    public void drawRoute() {
        appController.webEngine.executeScript("drawRoute("+ convertListToString(route.getPath()) +")");
    }

    private static String convertListToString(List<List<Double>> coordinatesList) {
        StringBuilder result = new StringBuilder("[");
        for (List<Double> coordinates : coordinatesList) {
            result.append("[")
                    .append(coordinates.get(0))
                    .append(", ")
                    .append(coordinates.get(1))
                    .append("], ");
        }
        // Remove the trailing comma and space
        if (coordinatesList.size() > 0) {
            result.setLength(result.length() - 2);
        }
        result.append("]");

        return result.toString();
    }

    public void loadReviews() {

        int routeID = this.route.getId();
        List<Integer> reviewsIDs = new ArrayList<>();
        String loadReviewIDsQuerry = "SELECT review_id FROM AS_RouteReview WHERE route_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement pstmt = connection.prepareStatement(loadReviewIDsQuerry)) {
            pstmt.setInt(1, routeID);
            ResultSet reviewsIDsSet = pstmt.executeQuery();
            while (reviewsIDsSet.next()) {
                reviewsIDs.add(reviewsIDsSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (int reviewID : reviewsIDs) {
            String loadReviewsQuerry = "SELECT * FROM Review WHERE id = ?";
            try (Connection connection = db.connect();
                 PreparedStatement pstmt = connection.prepareStatement(loadReviewsQuerry)) {
                pstmt.setInt(1,reviewID);
                ResultSet reviewsSet = pstmt.executeQuery();
                while (reviewsSet.next()) {
                    Review review = new Review();
                    review.setId(reviewsSet.getInt(1));
                    review.setReviewValue(reviewsSet.getInt(2));
                    //review.setDateTime(reviewsSet.getString(3));
                    review.setComment(reviewsSet.getString(4));
                    review.setAuthor(reviewsSet.getString(5));
                    this.route.addReview(review);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeReviews(Review review) {
        int latestID = 0;
        String getLatestIDQuerry = "SELECT MAX(id) FROM Review";
        try (Connection connection = db.connect();
             PreparedStatement pstmt = connection.prepareStatement(getLatestIDQuerry)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                latestID = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        latestID++;

        String writeReviewsQuery = "INSERT INTO Review(id, value, comment, username) VALUES (?, ?, ?,?)";
        try (Connection connection = db.connect();
             PreparedStatement pstmt = connection.prepareStatement(writeReviewsQuery)) {
            pstmt.setInt(1, latestID);
            pstmt.setInt(2, review.getReviewValue());
            pstmt.setString(3, review.getComment());
            pstmt.setString(4, review.getAuthor());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String writeReviewsRouteQuery = "INSERT INTO AS_RouteReview(route_id, review_id) VALUES (?, ?)";
        try (Connection connection = db.connect();
             PreparedStatement pstmt = connection.prepareStatement(writeReviewsRouteQuery)) {
            pstmt.setInt(1, route.getId());
            pstmt.setInt(2, latestID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Route getRoute() {
        return route;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

