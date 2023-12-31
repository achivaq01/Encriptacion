package com.project;

import java.io.File;
import java.security.PublicKey;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.project.encryption.FileEncrypter;
import com.project.encryption.HybridFileEncryption;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import static com.project.encryption.HybridFileEncryption.readPublicKey;

public class Controller1 implements initialize {

    @FXML
    private Button button0, button2;

    @FXML
    private Label label0;

    @FXML
    private TextField keyField, fileToEncryptField, fileOutputField;

    @FXML
    private void encrypt() {
        try {
            if (!new File(keyField.getText()).exists()) {
                label0.setText("CANT FIND THE KEY");
                label0.setVisible(true);
                return;
            }
            PublicKey publicKey = readPublicKey(keyField.getText());

            HybridFileEncryption.encryptFile(fileToEncryptField.getText(), fileOutputField.getText(), publicKey, "123");
            label0.setText("File Encrypted! The password is 123.");
            label0.setVisible(true);
        } catch (Exception e) {
            label0.setText("ERROR");
            label0.setVisible(true);
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void returnMenu() {
        UtilsViews.setViewAnimating("mainView");
    }

}