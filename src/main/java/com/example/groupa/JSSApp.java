package com.example.groupa;

import com.example.groupa.utils.InsertDataUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Team A
 * <p>
 * This is the main class for the javaFX application.\
 * </p>
 */
public class JSSApp extends Application {
    private InsertDataUtils insertDataUtils = new InsertDataUtils();
    double x, y = 0;

    @Override
    public void start(Stage stage) throws IOException {
        if (!insertDataUtils.checkForData()) {
            insertDataUtils.resetIncrements();
            insertDataUtils.readInAndLoadFiles();
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/base.fxml")));
        Scene scene = new Scene(root);

        root.setOnMouseClicked(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        stage.setScene(scene);
        stage.show();
    }
}