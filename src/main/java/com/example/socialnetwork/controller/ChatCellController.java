package com.example.socialnetwork.controller;

import com.example.socialnetwork.listener.ItemClickListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class ChatCellController {

    @FXML
    private AnchorPane itemView;
    @FXML
    private Label messageText;
    @FXML
    private Label nameText;

    private Long userId;
    private ItemClickListener itemClickListener;

    public void set(Long id, String firstName, String lastName, String message) {
        this.userId = id;
        nameText.setText(firstName + " " + lastName);
        messageText.setText(message);
    }

    @FXML
    private void onItemClick() {
        itemClickListener.onClick(userId);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
