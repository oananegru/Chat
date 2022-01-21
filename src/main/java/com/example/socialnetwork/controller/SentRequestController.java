package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.listener.SentRequestsListener;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SentRequestController {

    @FXML
    private JFXButton cancelButton;
    @FXML
    private Label nameText;

    SentRequestsListener sentRequestsListener;

    public void set(User user){
        nameText.setText(user.getFirstName() + " " + user.getLastName());
    }

    @FXML
    private void onCancelButtonClick() {
        sentRequestsListener.onCancelClick();
    }
    public void setOnSentRequestsClickListener(SentRequestsListener sentRequestsListener) {
        this.sentRequestsListener = sentRequestsListener;
    }
}
