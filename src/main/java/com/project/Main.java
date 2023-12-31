package com.project;//package com.project;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.stage.Stage;
//
//public class Main extends Application {
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//
//        final int windowWidth = 800;
//        final int windowHeight = 600;
//
//        UtilsViews.parentContainer.setStyle("-fx-font: 14 arial;");
//        UtilsViews.addView(getClass(), "View0", "/assets/view0.fxml");
//        UtilsViews.addView(getClass(), "View1", "/assets/view1.fxml");
//
//        Scene scene = new Scene(UtilsViews.parentContainer);
//
//        stage.setScene(scene);
//        stage.setTitle("Animació entre vistes");
//        stage.setMinWidth(windowWidth);
//        stage.setMinHeight(windowHeight);
//        stage.show();
//
//        // Add icon only if not Mac
//        if (!System.getProperty("os.name").contains("Mac")) {
//            Image icon = new Image("file:/icons/icon.png");
//            stage.getIcons().add(icon);
//        }
//    }
//}

import com.project.Utils.UtilsKeyPair;
import com.project.encryption.FileEncrypter;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        FileEncrypter fileEncrypter = new FileEncrypter();

        UtilsKeyPair.generateKeysToFile("data");
        InputStream publicKeyStream = new FileInputStream(new File("data/key_public.key"));
        fileEncrypter.setPublicKeyFile(publicKeyStream);
        OutputStream encryptFileStram = new FileOutputStream(new File("data/encrypt.txt"));
        fileEncrypter.setFileToEncrypt(encryptFileStram);

        fileEncrypter.encrypt();
        publicKeyStream.close();
        encryptFileStram.close();
    }
    public static void generateKeysToFile(String filePath) throws NoSuchAlgorithmException, IOException {
        File publicKeyFile = new File(filePath + "/key_public.key");
        File privateKeyFile = new File(filePath + "/key_private.key");

        KeyPair keys = generateKeys();
        var publicKey = keys.getPublic();
        var privateKey = keys.getPrivate();

        try (FileOutputStream fileOutputStream = new FileOutputStream(publicKeyFile)) {
            fileOutputStream.write(publicKey.getEncoded());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(privateKeyFile)) {
            fileOutputStream.write(privateKey.getEncoded());
        }
    }

    public static KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        return keyPairGenerator.generateKeyPair();
    }
}