package com.example.socialnetwork.ui;

import com.example.socialnetwork.controller.MaterialDialogController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class MaterialDialog {

    public static final int NORMAL_TIME = 2000;
    public static final int TYPE_INFO = 1;
    public static final int TYPE_SUCCESS = 2;
    public static final int TYPE_ERROR = 3;

    public static void showMessage(Stage stage, String message, int type, int time) {
        Stage dialoStage=new Stage();
        dialoStage.initOwner(stage);
        dialoStage.setResizable(false);
        dialoStage.initStyle(StageStyle.TRANSPARENT);

        double dialogX = dialoStage.getOwner().getX();
        double dialogY = dialoStage.getOwner().getY();
        double dialogW = dialoStage.getOwner().getWidth();
        double dialogH = dialoStage.getOwner().getHeight();

        double posX = dialogX + dialogW / 2 - 200;
        double posY = dialogY + dialogH/6 * 4.8;
        dialoStage.setX(posX);
        dialoStage.setY(posY);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MaterialDialogController.class.getResource("/view/material_dialog.fxml"));
            loader.load();

            MaterialDialogController controller = loader.getController();
            controller.setMessage(message);
            controller.setImage(MaterialDialog.getImage(type));

            Scene scene = new Scene(loader.getRoot());
            scene.setFill(Color.TRANSPARENT);
            dialoStage.setScene(scene);

            dialoStage.show();
            new Timeline(new KeyFrame(
                    Duration.millis(time),
                    ae -> {
                        dialoStage.close();
                    })).play();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static Image getImage(int type) {
       if (type == TYPE_SUCCESS) {
            return new Image(Objects.requireNonNull(MaterialDialog.class.getResourceAsStream("/images/check-success.png")));
        } else if (type == TYPE_ERROR) {
            return new Image(Objects.requireNonNull(MaterialDialog.class.getResourceAsStream("/images/error.png")));
        } else {
           return new Image(Objects.requireNonNull(MaterialDialog.class.getResourceAsStream("/images/info.png")));
       }
    }
}
