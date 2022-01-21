module com.example.socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires de.jensd.fx.glyphs.commons;
    requires com.jfoenix;

    opens com.example.socialnetwork to javafx.fxml;
    exports com.example.socialnetwork;
    exports com.example.socialnetwork.controller;
    exports com.example.socialnetwork.domain;
    exports com.example.socialnetwork.listener;
    opens com.example.socialnetwork.controller to javafx.fxml;
}