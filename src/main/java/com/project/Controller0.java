package com.project;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class Controller0 {

    @FXML
    private Button button0, button1;

    @FXML
    private void animateToEncrypt(ActionEvent event) {
        UtilsViews.setViewAnimating("encryptView");
    }

    @FXML
    private void animateToDecrypt(ActionEvent event) {
        UtilsViews.setViewAnimating("decryptView");
    }

}