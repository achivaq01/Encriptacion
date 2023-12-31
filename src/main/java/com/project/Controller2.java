package com.project;

import com.project.encryption.HybridFileEncryption;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.File;
import java.security.PrivateKey;

import static com.project.encryption.HybridFileEncryption.readPrivateKey;

public class Controller2 implements initialize {
    @FXML
    Button button0, button2;

    @FXML
    TextField keyField, fileToDecryptField, fileOutputField;

    @FXML
    PasswordField passwordField;

    @FXML
    Label label0;

    @FXML
    private  void decrypt() {
        try {
            if (!new File(keyField.getText()).exists()) {
                label0.setText("CANT FIND THE KEY");
                label0.setVisible(true);
                return;
            }
            PrivateKey privateKey = readPrivateKey(keyField.getText());

            HybridFileEncryption.decryptFile(fileToDecryptField.getText(), fileOutputField.getText(), privateKey, passwordField.getText());
            label0.setText("File Decrypted!");
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
