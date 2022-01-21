package com.example.socialnetwork.controller;

import com.example.socialnetwork.database.*;
import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.domain.validators.*;
import com.example.socialnetwork.listener.ObserverUpdateListener;
import com.example.socialnetwork.listener.ReceivedRequestListener;
import com.example.socialnetwork.listener.SentRequestsListener;
import com.example.socialnetwork.manager.SessionManager;
import com.example.socialnetwork.observer.Observer;
import com.example.socialnetwork.repository.base.DatabaseRepository;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultLoading;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.FriendshipRequestService;
import com.example.socialnetwork.service.FriendshipService;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class FriendsController {

    @FXML
    private ListView<HBox> friendsListView;
    @FXML
    private ListView<AnchorPane> requestsListView;
    @FXML
    private JFXButton receivedRequestsButton;
    @FXML
    private JFXButton sentRequestsButton;
    @FXML
    private AnchorPane container;

    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private ObservableList<AnchorPane> requestsListObserver;
    private ObservableList<HBox> friendsListObserver;
    private Observer<List<User>> receivedRequestObserver;
    private Observer<List<User>> sentRequestsObserver;
    private Observer<List<User>> friendsObserver;

    @FXML
    private void initialize() {
        requestsListObserver = FXCollections.observableArrayList();
        friendsListObserver = FXCollections.observableArrayList();
        receivedRequestObserver = new Observer<>();
        sentRequestsObserver = new Observer<>();
        friendsObserver = new Observer<>();

        requestsListView.setItems(requestsListObserver);
        friendsListView.setItems(friendsListObserver);

        // Received friend requests observer update
        receivedRequestObserver.setOnUpdateListener(new ObserverUpdateListener<>() {
            @Override
            public void onLoading() {
            }

            @Override
            public void onSuccess(ResultSuccess<List<User>> resultSuccess) {
                try {
                    updateReceivedRequestsList(resultSuccess.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResultError resultError) {
            }
        });

        // Sent friend requests observer update
        sentRequestsObserver.setOnUpdateListener(new ObserverUpdateListener<List<User>>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onSuccess(ResultSuccess<List<User>> resultSuccess) {
                try {
                    updateSentRequestsList(resultSuccess.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResultError resultError) {

            }
        });

        // Sent friends observer update
        friendsObserver.setOnUpdateListener(new ObserverUpdateListener<List<User>>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onSuccess(ResultSuccess<List<User>> resultSuccess) {
                try {
                    updateFriendsList(resultSuccess.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResultError resultError) {

            }
        });

        setService();

        // update requests
        receivedRequestObserver.setValue(new ResultLoading());
        receivedRequestObserver.setValue(friendshipRequestService.getRequestsForUser(SessionManager.getInstance().getUser().getId()));

        // update friends
        friendsObserver.setValue(new ResultLoading());
        friendsObserver.setValue(friendshipService.getFriendsForUser(SessionManager.getInstance().getUser().getId()));

    }

    @FXML
    private void onReceivedRequestsButtonClick(){
        deselect();
        receivedRequestsButton.getStyleClass().add("dark");
        receivedRequestObserver.setValue(friendshipRequestService.getRequestsForUser(SessionManager.getInstance().getUser().getId()));
    }

    @FXML
    private void onSentRequestsButtonClick() {
        deselect();
        sentRequestsButton.getStyleClass().add("dark");
        sentRequestsObserver.setValue(friendshipRequestService.getUserSentRequests(SessionManager.getInstance().getUser().getId()));
    }

    private void updateReceivedRequestsList(List<User> requests) throws IOException {
        requestsListObserver.clear();
        List<AnchorPane> data = new ArrayList<>();
        for(User user : requests) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FriendsController.class.getResource("/view/received_request_cell.fxml"));
            AnchorPane pane = loader.load();
            ReceivedRequestCellController controller = loader.getController();
            controller.setOnReceivedRequestClickListener(new ReceivedRequestListener() {
                @Override
                public void onAcceptClick() {
                    try {
                        answerFriendRequest(user.getId(), FriendshipRequest.APPROVED);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDeclineClick() {
                    try {
                        answerFriendRequest(user.getId(), FriendshipRequest.REJECTED);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            controller.set(user);
            data.add(pane);
        }

        requestsListObserver.setAll(data);
    }

    private void updateSentRequestsList(List<User> requests) throws IOException {
        requestsListObserver.clear();
        List<AnchorPane> data = new ArrayList<>();
        for(User user : requests) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FriendsController.class.getResource("/view/sent_request_cell.fxml"));
            AnchorPane pane = loader.load();
            SentRequestController controller = loader.getController();
            controller.setOnSentRequestsClickListener(new SentRequestsListener() {
                @Override
                public void onCancelClick() {
                    try {
                        deleteFriendRequest(user.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            controller.set(user);
            data.add(pane);
        }

        requestsListObserver.setAll(data);
    }

    private void updateFriendsList(List<User> friends) throws IOException {
        friendsListObserver.clear();
        List<HBox> data = new ArrayList<>();
        for(User user : friends) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FriendsController.class.getResource("/view/user_cell.fxml"));
            HBox hBox = loader.load();
            UserCellController controller = loader.getController();
            controller.set(user, true);
            data.add(hBox);
        }

        friendsListObserver.setAll(data);
    }

    private void setService() {
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FriendshipRequestValidator friendshipRequestValidator = new FriendshipRequestValidator();

        // Repository
        DatabaseRepository<User> userDatabaseRepository = new DatabaseRepository<>(userValidator, new UserDao());
        DatabaseRepository<Friendship> friendshipDatabaseRepository = new DatabaseRepository<>(friendshipValidator, new FriendshipDao());
        DatabaseRepository<FriendshipRequest> friendshipRequestDatabaseRepository = new DatabaseRepository<>(friendshipRequestValidator, new FriendshipRequestDao());

        friendshipService = new FriendshipService(friendshipDatabaseRepository,
                userDatabaseRepository);
        friendshipRequestService = new FriendshipRequestService(
                friendshipRequestDatabaseRepository, userDatabaseRepository);
    }

    private void deselect() {
        receivedRequestsButton.getStyleClass().remove("dark");
        sentRequestsButton.getStyleClass().remove("dark");
    }

    private void answerFriendRequest(Long userId, String status) throws IOException {
       Result requestResult = friendshipRequestService.get(userId, SessionManager.getInstance().getUser().getId());
       if (requestResult != null && requestResult.isSuccess()) {
           FriendshipRequest friendshipRequest = ((ResultSuccess<FriendshipRequest>) requestResult).getData();
           friendshipRequestService.updateStatus(friendshipRequest.getId(), status);
           receivedRequestObserver.setValue(friendshipRequestService.getRequestsForUser(SessionManager.getInstance().getUser().getId()));
           friendshipService.add(userId, SessionManager.getInstance().getUser().getId(), LocalDateTime.now());
           friendsObserver.setValue(friendshipService.getFriendsForUser(SessionManager.getInstance().getUser().getId()));
       }
    }

    private void deleteFriendRequest(Long userId) throws IOException {
        Result requestResult = friendshipRequestService.get(SessionManager.getInstance().getUser().getId(), userId);
        if (requestResult != null && requestResult.isSuccess()) {
            FriendshipRequest friendshipRequest = ((ResultSuccess<FriendshipRequest>) requestResult).getData();
            Result result = friendshipRequestService.delete(friendshipRequest.getId());
            sentRequestsObserver.setValue(friendshipRequestService.getUserSentRequests(SessionManager.getInstance().getUser().getId()));
        }
    }
}
