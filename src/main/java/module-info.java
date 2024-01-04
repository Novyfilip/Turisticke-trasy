module cz.vse.turistickaaplikace {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;

    opens cz.vse.turistickaaplikace to javafx.fxml;
    exports cz.vse.turistickaaplikace;
    exports cz.vse.turistickaaplikace.controllers;
    exports cz.vse.turistickaaplikace.components;
    exports cz.vse.turistickaaplikace.models;
    exports cz.vse.turistickaaplikace.enumerators;
    exports cz.vse.turistickaaplikace.interfaces;
    opens cz.vse.turistickaaplikace.controllers to javafx.fxml;
}