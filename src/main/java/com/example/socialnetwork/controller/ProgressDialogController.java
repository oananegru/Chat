package com.example.socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProgressDialogController {

    @FXML
    private Label messageLabel;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
