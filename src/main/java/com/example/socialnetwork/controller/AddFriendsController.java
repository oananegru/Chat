package com.example.socialnetwork.controller;

import com.example.socialnetwork.database.FriendshipDao;
import com.example.socialnetwork.database.FriendshipRequestDao;
import com.example.socialnetwork.database.UserDao;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.FriendshipRequest;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.FriendshipRequestValidator;
import com.example.socialnetwork.domain.validators.FriendshipValidator;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.listener.ObserverUpdateListener;
import com.example.socialnetwork.manager.SessionManager;
import com.example.socialnetwork.observer.Observer;
import com.example.socialnetwork.repository.base.DatabaseRepository;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.FriendshipRequestService;
import com.example.socialnetwork.service.FriendshipService;
import com.example.socialnetwork.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddFriendsController {

    @FXML
    private ListView<HBox> usersListView;

    private ObservableList<HBox> usersListObserver;
    private Observer<List<User>> usersObserver;
    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;

    @FXML
    private void initialize() {
        usersListObserver = FXCollections.observableArrayList();
        usersListView.setItems(usersListObserver);
        usersObserver = new Observer<>();

        usersObserver.setOnUpdateListener(new ObserverUpdateListener<List<User>>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onSuccess(ResultSuccess<List<User>> resultSuccess) {
                try {
                    updateListView(resultSuccess.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResultError resultError) {

            }
        });

        setService();
        usersObserver.setValue(userService.getAll());

    }

    private void updateListView(List<User> users) throws IOException {
        usersListObserver.clear();
        List<HBox> data = new ArrayList<>();
        for(User user : users) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AddFriendsController.class.getResource("/view/user_cell.fxml"));
            HBox hBox = loader.load();
            UserCellController controller = loader.getController();
            controller.set(user, false);
            if (!isFriend(user)) {
                data.add(hBox);
            }
        }

        usersListObserver.setAll(data);
    }

    private boolean isFriend(User user) {
        Result result = friendshipService.get(user.getId(), SessionManager.getInstance().getUser().getId());
        return result != null && result.isSuccess();
    }

    private void setService() {
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FriendshipRequestValidator friendshipRequestValidator = new FriendshipRequestValidator();

        // Repository
        DatabaseRepository<User> userDatabaseRepository = new DatabaseRepository<>(userValidator, new UserDao());

        userService = new UserService(userDatabaseRepository);

        // Repository
        DatabaseRepository<Friendship> friendshipDatabaseRepository = new DatabaseRepository<>(friendshipValidator, new FriendshipDao());
        DatabaseRepository<FriendshipRequest> friendshipRequestDatabaseRepository = new DatabaseRepository<>(friendshipRequestValidator, new FriendshipRequestDao());

        friendshipService = new FriendshipService(friendshipDatabaseRepository,
                userDatabaseRepository);
        friendshipRequestService = new FriendshipRequestService(
                friendshipRequestDatabaseRepository, userDatabaseRepository);
    }
}
