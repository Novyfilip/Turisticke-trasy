package cz.vse.turistickaaplikace.controllers;

import cz.vse.turistickaaplikace.enumerators.AppChange;
import cz.vse.turistickaaplikace.interfaces.IObservable;
import cz.vse.turistickaaplikace.interfaces.IObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.controlsfx.control.RangeSlider;

import java.net.URL;
import java.util.*;

public class FiltersController implements Initializable, IObservable {

    private AppController appController;

    @FXML
    private Pane distanceSliderPlaceholder;

    @FXML
    private RangeSlider distanceSlider;

    @FXML
    private Label distanceMinValue;

    @FXML
    private Label distanceMaxValue;

    @FXML
    private Double distanceMin = 0.0;

    @FXML
    private Double distanceMax = 50.0;

    @FXML
    private Slider reviewSlider;

    @FXML
    public Label reviewValue;

    @FXML
    private CheckBox easyCheckbox;

    @FXML
    private CheckBox mediumCheckbox;

    @FXML
    private CheckBox hardCheckbox;

    public Set<Integer> complexities = new HashSet<>();

    private Map<AppChange, Set<IObserver>> observerList = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (AppChange changeType: AppChange.values()) {
            observerList.put(changeType, new HashSet<>());
        }

        initDistanceSlider();
        initReviewSlider();
        handleComplexity();
    }

    public void setAppController(AppController controller) {
        appController = controller;
    }

    private void initDistanceSlider() {
        distanceMinValue.setText(distanceMin + " km");
        distanceMaxValue.setText(distanceMax + "+ km");

        distanceSlider = new RangeSlider(distanceMin, distanceMax, distanceMin, distanceMax);
        distanceSlider.prefWidthProperty().bind(distanceSliderPlaceholder.widthProperty());

        distanceSlider.lowValueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                distanceMinValue.setText(newValue.intValue() + " km");
            }
        });

        distanceSlider.highValueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                distanceMaxValue.setText((newValue.intValue() < distanceMax ? newValue.intValue() : newValue.intValue() + "+") + " km");
            }
        });

        distanceSlider.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            notifyObserver(AppChange.FILTERS_CHANGE);
        });

        distanceSliderPlaceholder.getChildren().addAll(distanceSlider);
    }

    private void initReviewSlider() {
        reviewSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!reviewSlider.isValueChanging()) { // Change reviewValue on snaps only
                if (newValue.intValue() < 1) {
                    reviewValue.setText("Jakékoliv");
                } else {
                    reviewValue.setText("Více než " + getReviewValue());
                }

                notifyObserver(AppChange.FILTERS_CHANGE);
            }
        });
    }

    @FXML
    private void handleComplexity() {
        if (easyCheckbox.isSelected()) {
            complexities.add(1);
        } else {
            complexities.remove(1);
        }

        if (mediumCheckbox.isSelected()) {
            complexities.add(2);
        } else {
            complexities.remove(2);
        }

        if (hardCheckbox.isSelected()) {
            complexities.add(3);
        } else {
            complexities.remove(3);
        }

        notifyObserver(AppChange.FILTERS_CHANGE);
    }

    public Set getComplexities() {
        return complexities;
    }

    public double getMinDistanceValue() {
        return Math.round(distanceSlider.getLowValue());
    }

    public double getMaxDistanceValue() {
        return distanceSlider.getHighValue() == distanceMax ? 99999.0 : Math.round(distanceSlider.getHighValue());
    }

    /**
     * Converts slider values 0, 1, 2, 3 to 0, 3.5, 4.0, 4.5
     * that will be used for sorting routes
     *
     * @return double
     */
    public double getReviewValue() {
        double amount = 0;

        switch ((int) reviewSlider.getValue()) {
            case 1:
                amount = 3.5;
                break;
            case 2:
                amount = 4.0;
                break;
            case 3:
                amount = 4.5;
                break;
        }

        return amount;
    }

    public void closeFiltersPanel(MouseEvent mouseEvent) {
        appController.closePanel();
    }

    @Override
    public void registruj(AppChange gameChange, IObserver observer) {
        observerList.get(gameChange).add(observer);
    }

    private void notifyObserver(AppChange gameChange) {
        for (IObserver observer : observerList.get(gameChange)) {
            observer.aktualizuj();
        }
    }
}