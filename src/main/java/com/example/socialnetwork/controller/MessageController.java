package com.example.socialnetwork.controller;

import com.example.socialnetwork.database.*;
import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.domain.validators.*;
import com.example.socialnetwork.listener.ItemClickListener;
import com.example.socialnetwork.listener.ObserverUpdateListener;
import com.example.socialnetwork.manager.SessionManager;
import com.example.socialnetwork.observer.Observer;
import com.example.socialnetwork.repository.base.DatabaseRepository;
import com.example.socialnetwork.result.Result;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultLoading;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.*;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class MessageController {

    @FXML
    private JFXButton sendButton;
    @FXML
    private TextField messageTextField;
    @FXML
    private ListView<AnchorPane> conversationListView;
    @FXML
    private ListView<AnchorPane> chatListView;

    private UserService userService;
    private MessageService messageService;
    private Chat chat;
    private ObservableList<AnchorPane> conversationListObserver;
    private ObservableList<AnchorPane> chatListObserver;
    private Observer<List<Message>> messageListObserver;
    private Observer<Map<Long, List<String>>> chatItemsObserver;
    private Long conversationUserId;

    @FXML
    private void initialize() {
        conversationListObserver = FXCollections.observableArrayList();
        chatListObserver = FXCollections.observableArrayList();

        // set listviews
        conversationListView.setFocusTraversable(false);
        chatListView.setFocusTraversable(false);
        conversationListView.setItems(conversationListObserver);
        chatListView.setItems(chatListObserver);

        messageListObserver = new Observer<>();
        chatItemsObserver = new Observer<>();

        messageListObserver.setOnUpdateListener(new ObserverUpdateListener<List<Message>>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onSuccess(ResultSuccess<List<Message>> resultSuccess) {
                try {
                    updateMessageList(resultSuccess.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResultError resultError) {

            }
        });

        chatItemsObserver.setOnUpdateListener(new ObserverUpdateListener<Map<Long, List<String>>>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onSuccess(ResultSuccess<Map<Long, List<String>>> resultSuccess) {
                try {
                    updateChatList(resultSuccess.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ResultError resultError) {

            }
        });

        setService();

        // messageListObserver.setValue(new ResultLoading());
        // messageListObserver.setValue(chat.getConversation(2L, SessionManager.getInstance().getUser().getId()));

        chatItemsObserver.setValue(new ResultLoading());
        chatItemsObserver.setValue(chat.getAllForUser(SessionManager.getInstance().getUser().getId()));
    }

    @FXML
    private void onSendButtonClick() throws IOException {
        sendMessage();
        messageListObserver.setValue(chat.getConversation(conversationUserId, SessionManager.getInstance().getUser().getId()));
        chatItemsObserver.setValue(chat.getAllForUser(SessionManager.getInstance().getUser().getId()));
        messageTextField.setText("");
    }

    private void updateMessageList(List<Message> messages) throws IOException {
        conversationListObserver.clear();
        List<AnchorPane> data = new ArrayList<>();
        for(Message message : messages) {
            AnchorPane pane = getMessageView(message);
            data.add(pane);
        }
        conversationListObserver.setAll(data);
        conversationListView.scrollTo(data.size() - 1);
    }

    private void updateChatList(Map<Long, List<String>> chatMap) throws IOException {
        chatListObserver.clear();
        List<AnchorPane> data = new ArrayList<>();
        for(Long chatId : chatMap.keySet()) {
            List<String> chatItem = chatMap.get(chatId);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MessageController.class.getResource("/view/chat_cell.fxml"));
            AnchorPane pane = loader.load();
            ChatCellController controller = loader.getController();
            controller.set(chatId, chatItem.get(0), chatItem.get(1), chatItem.get(2));
            controller.setOnItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(Long id) {
                    conversationUserId = id;
                    messageListObserver.setValue(chat.getConversation(id, SessionManager.getInstance().getUser().getId()));
                }
            });
            data.add(pane);
        }

        chatListObserver.setAll(data);
    }

    private AnchorPane getMessageView(Message message) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        if (Objects.equals(message.getFrom(), SessionManager.getInstance().getUser().getId())) {
            loader.setLocation(MessageController.class.getResource("/view/sent_message_view.fxml"));
            AnchorPane pane = loader.load();
            SentMessageController controller = loader.getController();
            controller.set(message.getMessage());
            return pane;
        } else {
            loader.setLocation(MessageController.class.getResource("/view/received_message_view.fxml"));
            AnchorPane pane = loader.load();
            ReceivedMessageController controller = loader.getController();
            controller.set(message.getMessage());
            return pane;
        }
    }

    private void setService() {
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        MessageValidator messageValidator = new MessageValidator();
        FriendshipRequestValidator friendshipRequestValidator = new FriendshipRequestValidator();
        UserMessagesValidator userMessagesValidator = new UserMessagesValidator();

        try {
            // Repository
            // UserFileRepository userFileRepository = new UserFileRepository(userValidator, "Users.json");
            DatabaseRepository<User> userDatabaseRepository = new DatabaseRepository<>(userValidator, new UserDao());
            // FriendshipFileRepository friendshipFileRepository = new FriendshipFileRepository(friendshipValidator, "Friendships.json");
            DatabaseRepository<Friendship> friendshipDatabaseRepository = new DatabaseRepository<>(friendshipValidator, new FriendshipDao());

            DatabaseRepository<FriendshipRequest> friendshipRequestDatabaseRepository = new DatabaseRepository<>(friendshipRequestValidator, new FriendshipRequestDao());

            DatabaseRepository<Message> messageDatabaseRepository =
                    new DatabaseRepository<>(messageValidator, new MessageDao());

            DatabaseRepository<UserMessages> userMessagesDatabaseRepository =
                    new DatabaseRepository<>(userMessagesValidator, new UserMessagesDao());
            // Service
            userService = new UserService(userDatabaseRepository);
            FriendshipService friendshipService = new FriendshipService(friendshipDatabaseRepository, userDatabaseRepository);
            FriendshipRequestService friendshipRequestService = new FriendshipRequestService(friendshipRequestDatabaseRepository, userDatabaseRepository);
            messageService = new MessageService(messageDatabaseRepository, userDatabaseRepository, userMessagesDatabaseRepository);
            chat = new Chat(userMessagesDatabaseRepository, userDatabaseRepository, messageDatabaseRepository);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendMessage() {
        try {
            String messageText = messageTextField.getText();
            addMessage(messageText, SessionManager.getInstance().getUser().getId(), LocalDateTime.now(), 0);
            Result result = chat.sendMessage(conversationUserId, messageService.getLastMessageId());
            if (result.isSuccess()) {
                System.out.println("ok");
            } else {
                System.out.println(((ResultError) result).getError());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void addMessage(String messageText, Long from, LocalDateTime date, int reply) {
        Result resultMessage = null;
        try {
            resultMessage = messageService.add(messageText, from, date, reply);
            if (resultMessage.isSuccess()) {
                System.out.println("ok");
            } else {
                System.out.println(((ResultError) resultMessage).getError());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
