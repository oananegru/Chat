package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.listener.ReceivedRequestListener;
import com.example.socialnetwork.service.FriendshipService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReceivedRequestCellController {


    @FXML
    private Label nameText;
    @FXML
    private JFXButton acceptButton;
    @FXML
    private JFXButton declineButton;

    private ReceivedRequestListener receivedRequestListener;

    public void set(User user) {
        nameText.setText(user.getFirstName() + " " + user.getLastName());
    }

    public void setOnReceivedRequestClickListener(ReceivedRequestListener receivedRequestListener) {
        this.receivedRequestListener = receivedRequestListener;
    }

    @FXML
    private void onAcceptButtonClick() {
        receivedRequestListener.onAcceptClick();
    }

    @FXML
    private void onDeclineButtonClick() {
        receivedRequestListener.onDeclineClick();
    }
}
