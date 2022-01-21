package com.example.socialnetwork.controller;

import com.example.socialnetwork.listener.ReceivedRequestListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReceivedMessageController {

    @FXML
    private Label messageText;

    public void set(String message) {
        messageText.setText(message);
    }
}
