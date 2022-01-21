package com.example.socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SentMessageController {

    @FXML
    private Label messageText;

    public void set(String message) {
        messageText.setText(message);
    }
}
