package com.example.socialnetwork.ui;

import com.example.socialnetwork.controller.ProgressDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ProgressDialog {

    private static Stage dialogStage;

    public static void show(Stage stage) {
        show(stage, null);
    }

    public static void show(Stage stage, String customMessage) {
        dialogStage = new Stage();
        dialogStage.initOwner(stage);
        dialogStage.setResizable(false);
        dialogStage.initStyle(StageStyle.TRANSPARENT);

        double dialogX = dialogStage.getOwner().getX();
        double dialogY = dialogStage.getOwner().getY();
        double dialogW = dialogStage.getOwner().getWidth();
        double dialogH = dialogStage.getOwner().getHeight();

        double posX = dialogX + dialogW / 2 - 100;
        double posY = dialogY + dialogH/6 * 4.8;
        dialogStage.setX(posX);
        dialogStage.setY(posY);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ProgressDialogController.class.getResource("/view/progress_dialog.fxml"));
            loader.load();

            ProgressDialogController controller = loader.getController();
            if (customMessage != null) {
                controller.setMessage(customMessage);
            }

            Scene scene = new Scene(loader.getRoot());
            scene.setFill(Color.TRANSPARENT);
            dialogStage.setScene(scene);

            dialogStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void dismiss(){
        dialogStage.hide();
    }
}
