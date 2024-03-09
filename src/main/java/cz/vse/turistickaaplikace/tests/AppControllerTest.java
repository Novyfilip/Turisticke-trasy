package cz.vse.turistickaaplikace.tests;
import cz.vse.turistickaaplikace.controllers.AppController;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AppControllerTest {
    private AppController appController;

    @BeforeEach
    void setUp() {
        appController = new AppController();
        appController.routes = new VBox();
        appController.filters = new VBox();
        appController.routeDetails = new VBox();
    }

    @Test
    void testOpenFiltersPanel() {
        appController.openFiltersPanel();
        assertTrue(appController.filters.isVisible());
        assertFalse(appController.routes.isVisible());
    }

    @Test
    void testOpenRouteDetailsPanel() {
        appController.openRouteDetailsPanel();
        assertTrue(appController.routeDetails.isVisible());
        assertFalse(appController.routes.isVisible());
    }

    @Test
    void testClosePanel() {
        appController.closePanel();
        assertFalse(appController.filters.isVisible());
        assertFalse(appController.routeDetails.isVisible());
        assertTrue(appController.routes.isVisible());
    }
}

