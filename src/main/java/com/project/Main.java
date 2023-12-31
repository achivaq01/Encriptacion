package com.project;//package com.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        final int windowWidth = 600;
        final int windowHeight = 400;

        UtilsViews.parentContainer.setStyle("-fx-font: 14 arial;");
        UtilsViews.addView(getClass(), "mainView", "/assets/mainView.fxml");
        UtilsViews.addView(getClass(), "encryptView", "/assets/encryptView.fxml");
        UtilsViews.addView(getClass(), "decryptView", "/assets/decryptView.fxml");

        Scene scene = new Scene(UtilsViews.parentContainer);

        stage.setScene(scene);
        stage.setTitle("Animació entre vistes");
        stage.setMinWidth(windowWidth);
        stage.setMinHeight(windowHeight);
        stage.show();

        // Add icon only if not Mac
        if (!System.getProperty("os.name").contains("Mac")) {
            Image icon = new Image("file:/icons/icon.png");
            stage.getIcons().add(icon);
        }
    }
}

