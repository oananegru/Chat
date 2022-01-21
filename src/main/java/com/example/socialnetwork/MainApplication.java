package com.example.socialnetwork;

import com.example.socialnetwork.database.*;
import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.domain.validators.*;
import com.example.socialnetwork.repository.base.DatabaseRepository;
import com.example.socialnetwork.service.*;
import com.example.socialnetwork.ui.NewUi;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/view/main_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1173, 754);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        Connection connection = new DBConnect().connection();

        /*
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        MessageValidator messageValidator = new MessageValidator();
        FriendshipRequestValidator friendshipRequestValidator = new FriendshipRequestValidator();
        UserMessagesValidator userMessagesValidator = new UserMessagesValidator();

        // insertUser(new User(1L, "test", "test", "test@gmail.com", "1"), connection);

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
            UserService userService = new UserService(userDatabaseRepository);
            FriendshipService friendshipService = new FriendshipService(friendshipDatabaseRepository,
                    userDatabaseRepository);
            FriendshipRequestService friendshipRequestService = new FriendshipRequestService(
                    friendshipRequestDatabaseRepository, userDatabaseRepository);
            MessageService messageService = new MessageService(messageDatabaseRepository, userDatabaseRepository,
                    userMessagesDatabaseRepository);
            Chat chat = new Chat(userMessagesDatabaseRepository, userDatabaseRepository, messageDatabaseRepository);

            // Ui
            NewUi ui = new NewUi(userService, friendshipService, friendshipRequestService, messageService, chat);
            ui.showMenu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
    }
}