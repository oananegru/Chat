package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.listener.NavbarItemChangeListener;
import com.example.socialnetwork.manager.SessionManager;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomeController {

    private static final int CHAT = 0;
    private static final int FRIENDS = 1;
    private static final int ADD_FRIENDS = 2;

    @FXML
    private AnchorPane container;
    @FXML
    private Label nameText;
    @FXML
    private JFXButton navbarItem1;
    @FXML
    private JFXButton navbarItem2;
    @FXML
    private JFXButton navbarItem3;
    @FXML
    private JFXButton profileButton;

    private AnchorPane chatPage;
    private AnchorPane friendsPage;
    private AnchorPane addFriendsPage;

    private User user;
    private NavbarItemChangeListener navbarItemChangeListener;

    @FXML
    private void initialize() throws IOException {
        this.user = SessionManager.getInstance().getUser();
        nameText.setText(user.getFirstName());

        setChatPage();
        setFriendsPage();
        setAddFriendsPage();

        navbarItemChangeListener = position -> {
            switch (position) {
                case CHAT:
                    container.getChildren().clear();
                    container.getChildren().add(chatPage);
                    break;
                case FRIENDS:
                    container.getChildren().clear();
                    container.getChildren().add(friendsPage);
                    break;
                case ADD_FRIENDS:
                    container.getChildren().clear();
                    container.getChildren().add(addFriendsPage);
                    break;
            }
        };

        navbarItemChangeListener.onItemSelected(0);
    }

    @FXML
    private void navbarItem1Click() {
        navbarItemChangeListener.onItemSelected(CHAT);
        deselect();
        navbarItem1.getStyleClass().add("nav-bar-button-selected");
    }

    @FXML
    private void navbarItem2Click() {
        navbarItemChangeListener.onItemSelected(FRIENDS);
        deselect();
        navbarItem2.getStyleClass().add("nav-bar-button-selected");
    }

    @FXML
    private void navbarItem3Click() {
        navbarItemChangeListener.onItemSelected(ADD_FRIENDS);
        deselect();
        navbarItem3.getStyleClass().add("nav-bar-button-selected");
    }

    private void deselect() {
        navbarItem1.getStyleClass().remove("nav-bar-button-selected");
        navbarItem2.getStyleClass().remove("nav-bar-button-selected");
        navbarItem3.getStyleClass().remove("nav-bar-button-selected");
    }

    private void setChatPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/view/messages.fxml"));
        chatPage = loader.load();
    }

    private void setFriendsPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/view/friends_screen.fxml"));
        friendsPage = loader.load();
    }

    private void setAddFriendsPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/view/add_friends_screen.fxml"));
        addFriendsPage = loader.load();
    }
}
