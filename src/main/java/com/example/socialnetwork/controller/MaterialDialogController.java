package com.example.socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MaterialDialogController {

    @FXML
    private ImageView imageView;
    @FXML
    private Label messageLabel;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }
}
