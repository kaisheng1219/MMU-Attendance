package org.doodlediary.tools.qreader.components;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class CredentialInputDialog extends Dialog<Pair<String, String>> {
    public CredentialInputDialog(String username, String password) {
        setTitle("Credentials");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField tfStudentId = new TextField();
        tfStudentId.setPromptText("Student Id");
        tfStudentId.setText(username);
        gridPane.add(new Label("Student Id"), 0, 0);
        gridPane.add(tfStudentId, 1, 0);

        TextField tfPassword = new PasswordField();
        tfPassword.setPromptText("Password");
        tfPassword.setText(password);
        gridPane.add(new Label("Password"), 0, 1);
        gridPane.add(tfPassword, 1, 1);

        getDialogPane().setContent(gridPane);
        Platform.runLater(() -> gridPane.getChildren().get(0).requestFocus());

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(tfStudentId.getText(), tfPassword.getText());
            }
            return null;
        });
    }
}
