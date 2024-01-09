package cz.vse.turistickaaplikace.models;

import cz.vse.turistickaaplikace.components.DatabaseComponent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Route {

    private int id;

    private String title;

    private String description;

    private double timeToComplete;

    private double distance;

    private int complexity;

    private String region;

    private String mountains;

    private String url;

    private List<List<Double>> path;

    private List<Review> reviews = new ArrayList<>();

    private List<Route> similarRoutes = new ArrayList<>();

    public Route() {}

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getExcerpt() {
        int maxExcerptLength = 180;
        if (description.length() <= maxExcerptLength) {
            return description;
        } else {
            return description.substring(0, maxExcerptLength) + "...";
        }
    }

    public double getTimeToComplete() {
        return timeToComplete;
    }

    public String getDistance() {
        return distance + " km";
    }

    public Double getDistanceValue() {
        return distance;
    }

    public Double getReviewMeanValue() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;

        for (Review review : reviews) {
            sum += review.getReviewValue();
        }

        return sum / reviews.size();
    }

    public String getComplexity() {
        String level = switch (complexity) {
            default -> "Mírná";
            case 2 -> "Střední";
            case 3 -> "Těžká";
        };
        return level + " náročnost";
    }

    public Integer getComplexityValue() {
        return complexity;
    }

    public Paint getComplexityColor() {
        Paint color = switch (complexity) {
            default -> Color.web("0x639754",1.0);
            case 2 -> Color.web("0xFFD301",1.0);
            case 3 -> Color.web("0xE03C32",1.0);
        };
        return color;
    }

    public String getRegion() {
        return region;
    }

    public String getMountains() {
        return mountains;
    }

    public String getUrl() {
        return url;
    }

    public boolean loadPath() {
        DatabaseComponent db = new DatabaseComponent();
        String loadPathQuerry = "SELECT * FROM Path WHERE routeID = ?";
        try (Connection connection = db.connect();
             PreparedStatement pstmt = connection.prepareStatement(loadPathQuerry)) {
            pstmt.setInt(1, this.getId());
            ResultSet pathResult = pstmt.executeQuery();
            List<List<Double>> path = new ArrayList<>();
            while (pathResult.next()) {
                double x = pathResult.getDouble("x");
                double y = pathResult.getDouble("y");
                List<Double> point = new ArrayList<>();
                point.add(x);
                point.add(y);
                path.add(point);
                this.setPath(path);
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<List<Double>> getPath() {
        loadPath();
        return path;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Route> getSimilarRoutes() {
        return similarRoutes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeToComplete(double timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setMountains(String mountains) {
        this.mountains = mountains;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(List<List<Double>> path) { this.path = path; }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void setSimilarRoutes(List<Route> similarRoutes) {
        this.similarRoutes = similarRoutes;
    }
}
