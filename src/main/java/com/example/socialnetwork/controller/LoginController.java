package com.example.socialnetwork.controller;

import com.example.socialnetwork.database.UserDao;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.manager.SessionManager;
import com.example.socialnetwork.observer.Observer;
import com.example.socialnetwork.listener.ObserverUpdateListener;
import com.example.socialnetwork.repository.base.DatabaseRepository;
import com.example.socialnetwork.result.ResultError;
import com.example.socialnetwork.result.ResultLoading;
import com.example.socialnetwork.result.ResultSuccess;
import com.example.socialnetwork.service.UserService;
import com.example.socialnetwork.ui.MaterialDialog;
import com.example.socialnetwork.ui.ProgressDialog;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    private Observer<User> userObserver;
    private UserService userService;
    @FXML
    private void initialize() {
        UserValidator userValidator = new UserValidator();
        DatabaseRepository<User> userRepository = new DatabaseRepository<>(userValidator, new UserDao());
        userService = new UserService(userRepository);
        userObserver = new Observer<>();

        userObserver.setOnUpdateListener(new ObserverUpdateListener<>() {
            @Override
            public void onLoading() {
                ProgressDialog.show((Stage) loginButton.getScene().getWindow());
            }

            @Override
            public void onSuccess(ResultSuccess<User> resultSuccess) {
                // TODO: 08.01.2022
                ProgressDialog.dismiss();
                SessionManager.getInstance().setUser(resultSuccess.getData());
                try {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/view/home_screen.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 1173, 754);
                    stage.setScene(scene);
                } catch (Exception e) {
                    MaterialDialog.showMessage((Stage) loginButton.getScene().getWindow(), "Something went wrong!", MaterialDialog.TYPE_ERROR, MaterialDialog.NORMAL_TIME);
                    e.printStackTrace();
                }

                MaterialDialog.showMessage((Stage) loginButton.getScene().getWindow(), "Successful", MaterialDialog.TYPE_SUCCESS, MaterialDialog.NORMAL_TIME);
                ProgressDialog.dismiss();
            }

            @Override
            public void onError(ResultError resultError) {
                ProgressDialog.dismiss();
                MaterialDialog.showMessage((Stage) loginButton.getScene().getWindow(), "Invalid email or password!", MaterialDialog.TYPE_ERROR, MaterialDialog.NORMAL_TIME);
            }
        });
    }

    @FXML
    private void onLoginButtonClick() {
        String email = emailTextField.getText();
        String password = passwordField.getText();
        try {
            userObserver.setValue(new ResultLoading());
            new Timeline(new KeyFrame(
                    Duration.millis(500),
                    ae -> {
                        userObserver.setValue(userService.get(email, password));
                    })).play();
        } catch (Exception e) {
            ProgressDialog.dismiss();
            e.printStackTrace();
        }
    }
}
