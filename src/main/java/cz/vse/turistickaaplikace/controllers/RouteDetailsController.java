package cz.vse.turistickaaplikace.controllers;

import cz.vse.turistickaaplikace.components.EmptyComponent;
import cz.vse.turistickaaplikace.components.ReviewComponent;
import cz.vse.turistickaaplikace.components.RouteComponent;
import cz.vse.turistickaaplikace.models.Review;
import cz.vse.turistickaaplikace.models.Route;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class RouteDetailsController implements Initializable {

    public Label titleLabel;

    public ScrollPane similarRoutesList;

    public ScrollPane reviewsList;

    public Label distanceLabel;

    public Label complexityLabel;

    public Circle complexityIcon;

    public Label reviewLabel;

    public Text descriptionLabel;
    public TextField komentareText;
    public Slider sliderHodnoceni;
    public Button buttonOdesli;

    public VBox reviewForm;

    public Label reviewFormMessage;

    private AppController appController;

    private RouteComponent routeComponent;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setRouteComponent(RouteComponent routeComponent) {
        this.routeComponent = routeComponent;
    }

    public void fillRoute(Route route) {
        titleLabel.setText(route.getTitle());
        reviewLabel.setText(String.valueOf(route.getReviewMeanValue()));
        distanceLabel.setText(route.getDistance());
        complexityLabel.setText(route.getComplexity());
        complexityIcon.setFill(route.getComplexityColor());
        descriptionLabel.setText(route.getDescription());

        if (route.getSimilarRoutes().isEmpty()) {
            similarRoutesList.setContent(new EmptyComponent());
        } else {
            HBox elements = new HBox();
            for (Route similarRoute : route.getSimilarRoutes()) {
                RouteComponent component = new RouteComponent(similarRoute, "routeNarrow");
                component.setAppController(appController);
                elements.getChildren().add(component);
            }
            similarRoutesList.setContent(elements);
        }
        refreshReviews(route);
        refreshCommentField();
    }

    public void refreshReviews(Route route) {
        if (route.getReviews().isEmpty()) {
            reviewsList.setContent(new EmptyComponent());
        } else {
            VBox elements = new VBox();
            for (Review review : route.getReviews()) {
                ReviewComponent component = new ReviewComponent(review);
                component.setAppController(appController);
                elements.getChildren().add(component);
            }
            reviewsList.setContent(elements);
        }
    }

    public void refreshCommentField() {
        if (appController.isUserLoggedIn()) {
            reviewForm.setDisable(false);
            reviewFormMessage.setTextFill(Paint.valueOf("black"));
            reviewFormMessage.setText("");
            reviewFormMessage.setManaged(false);
        } else {
            reviewForm.setDisable(true);
            reviewFormMessage.setManaged(true);
            reviewFormMessage.setTextFill(Paint.valueOf("red"));
            reviewFormMessage.setText("Abyste mohli přidávat hodnocení, zaregistrujte se nebo se přihlaste.");
        }
    }

    public void setAppController(AppController controller) {
        appController = controller;
    }

    public void closeRouteDetailPanel(MouseEvent mouseEvent) {
        appController.openRoutesPanel();
        appController.webEngine.executeScript("clearMap()");
        appController.webEngine.executeScript("recenterMap()");

    }

    public void odesliClick(MouseEvent mouseEvent) {
        //TODO vyresit DateTime
        komentareText.setDisable(true);
        Review review = new Review();
        /*review.setDateTime();*/
        review.setReviewValue(Integer.valueOf((int) sliderHodnoceni.getValue()));
        review.setComment(komentareText.getText());
        review.setAuthor(appController.getLoggedUser().getUsername());
        //review.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).trim());
        routeComponent.writeReviews(review);
        routeComponent.loadReviews();
        refreshReviews(routeComponent.getRoute());
        komentareText.clear();
        komentareText.setDisable(false);
        sliderHodnoceni.setValue(1);
    }
}
