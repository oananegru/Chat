package com.example.socialnetwork.controller;

import com.example.socialnetwork.database.UserDao;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validators.UserValidator;
import com.example.socialnetwork.domain.validators.base.ValidationException;
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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;

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
                ProgressDialog.show((Stage) registerButton.getScene().getWindow());
            }

            @Override
            public void onSuccess(ResultSuccess<User> resultSuccess) {
                ProgressDialog.dismiss();
                MaterialDialog.showMessage((Stage) registerButton.getScene().getWindow(), "Successful registered!",
                        MaterialDialog.TYPE_SUCCESS, MaterialDialog.NORMAL_TIME);
            }

            @Override
            public void onError(ResultError resultError) {
                ProgressDialog.dismiss();
                MaterialDialog.showMessage((Stage) registerButton.getScene().getWindow(), resultError.getError(),
                        MaterialDialog.TYPE_ERROR, MaterialDialog.NORMAL_TIME);
            }
        });
    }

    @FXML
    private void onRegisterButtonClick() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        try {
            //if (!"".equals(firstName) && !"".equals(lastName) && !"".equals(email) && !"".equals(password)) {
                userObserver.setValue(new ResultLoading()); new Timeline(new KeyFrame(
                    Duration.millis(500),
                    ae -> {
                        try {
                            userObserver.setValue(userService.add(firstName, lastName, email, password));
                        } catch (IOException e) {
                            ProgressDialog.dismiss();
                            e.printStackTrace();
                        } catch (ValidationException ve) {
                            ProgressDialog.dismiss();
                            ProgressDialog.dismiss();
                            MaterialDialog.showMessage((Stage) registerButton.getScene().getWindow(), ve.getMessage(),
                                    MaterialDialog.TYPE_ERROR, MaterialDialog.NORMAL_TIME);
                            ve.printStackTrace();
                        }

                    })).play();
            //}
        } catch (Exception e) {
            ProgressDialog.dismiss();
            e.printStackTrace();
        }
    }
}
