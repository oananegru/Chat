package com.example.socialnetwork.controller;

import com.example.socialnetwork.database.FriendshipDao;
import com.example.socialnetwork.database.FriendshipRequestDao;
import com.example.socialnetwork.database.UserDao;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.FriendshipRequest;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.*;
import com.example.socialnetwork.manager.SessionManager;
import com.example.socialnetwork.repository.base.DatabaseRepository;
import com.example.socialnetwork.service.FriendshipService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserCellController {

    @FXML
    private JFXButton addButton;
    @FXML
    private Label nameText;

    private FriendshipService friendshipService;

    @FXML
    private void initialize() {
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FriendshipRequestValidator friendshipRequestValidator = new FriendshipRequestValidator();

        // Repository
        DatabaseRepository<User> userDatabaseRepository = new DatabaseRepository<>(userValidator, new UserDao());
        DatabaseRepository<Friendship> friendshipDatabaseRepository = new DatabaseRepository<>(friendshipValidator, new FriendshipDao());

        friendshipService = new FriendshipService(friendshipDatabaseRepository, userDatabaseRepository);
    }

    public void set(User user, boolean friends) {
        nameText.setText(user.getFirstName() + " " + user.getLastName());
        if (friendshipService.get(user.getId(), SessionManager.getInstance().getUser().getId()).isSuccess()) {
            addButton.setVisible(!friends);
        }
    }
}
