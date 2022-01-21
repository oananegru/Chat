package com.example.socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private Label registerLabel;
    @FXML
    private VBox container;
    @FXML
    private Button registerButton;
    @FXML
    private ImageView backButton;

    private AnchorPane loginForm;
    private AnchorPane registerForm;

    @FXML
    protected void initialize() throws IOException {
        setLoginForm();
        setRegisterForm();
        backButton.setVisible(false);
        container.getChildren().add(loginForm);
    }

    @FXML
    private void onRegisterButtonClick() {
        container.getChildren().remove(loginForm);
        container.getChildren().add(registerForm);
        backButton.setVisible(true);
        registerLabel.setVisible(false);
        registerButton.setVisible(false);
    }

    @FXML
    private void onBackButtonClick() {
        container.getChildren().remove(registerForm);
        container.getChildren().add(loginForm);
        backButton.setVisible(false);
        registerLabel.setVisible(true);
        registerButton.setVisible(true);
    }

    private void setLoginForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/view/login_form.fxml"));
        loginForm = loader.load();
    }

    private void setRegisterForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/view/register_form.fxml"));
        registerForm = loader.load();
    }
}