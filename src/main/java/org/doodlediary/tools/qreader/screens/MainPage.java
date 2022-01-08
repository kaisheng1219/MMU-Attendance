package org.doodlediary.tools.qreader.screens;

import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Pair;
import org.doodlediary.tools.qreader.components.CredentialInputDialog;
import org.doodlediary.tools.qreader.components.ExceptionDialog;
import org.doodlediary.tools.qreader.utils.*;
import org.w3c.dom.Element;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

public class MainPage extends VBox {
    private final WebView webView;
    private final PreferencesHelper preferencesHelper = new PreferencesHelper();
    private String url;

    public MainPage() {
        Button btnCapture = new Button("Capture");
        btnCapture.setOnAction(e -> {
            capture();
        });

        Button btnEdit = new Button("Edit");
        btnEdit.setOnAction(e -> {
            CredentialInputDialog dialog = new CredentialInputDialog(
                    preferencesHelper.getPref("MMU_SID"),
                    preferencesHelper.getPref("MMU_PWD")
            );
            Optional<Pair<String, String>> result = dialog.showAndWait();
            if (result.isPresent()) {
                preferencesHelper.setPref("MMU_SID", result.get().getKey());
                preferencesHelper.setPref("MMU_PWD", result.get().getValue());
            }
        });

        Button btnRetry = new Button("Retry");
        btnRetry.setOnAction(e -> {
            getWebPage();
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(btnCapture, btnEdit, btnRetry);
        hBox.setStyle("-fx-margin: 10");
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);

        webView = new WebView();
        webView.setVisible(false);
        webView.managedProperty().bind(webView.visibleProperty());
        btnRetry.managedProperty().bind(webView.visibleProperty());
        btnRetry.visibleProperty().bind(webView.visibleProperty());

        setStyle("-fx-padding: 10");
        getChildren().addAll(hBox, webView);

        capture();
    }

    private void capture() {
        try {
            BufferedImage screenshot = Screenshoter.takeScreenshot();
            url = QRReader.readQRCode(screenshot);
            if (url != null && !url.equals("")) {
                SystemHelper.copyToClipboard(url);
                getWebPage();
            }
        } catch (Exception e) {
            new ExceptionDialog(e).showAndWait();
        }
    }


    private void getWebPage() {
        // credentials are stored in env
        String username = preferencesHelper.getPref("MMU_SID");
        String password = preferencesHelper.getPref("MMU_PWD");

        // solution to 'unable to connect to SSL services due to "PKIX Path Building Failed" error'
        WebHelper.setTrustManager();

        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);

        // autofill credentials and submit the form
        webEngine.getLoadWorker().stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                Element element = (Element) webEngine.getDocument().getElementsByTagName("form").item(0);
                if (element.getAttribute("action").equals("https://mmls2.mmu.edu.my/attendance/login")) {
                    String js = "document.getElementsByName('email')[0].value='" + username + "';"
                            + "document.getElementsByName('password')[0].value='" + password + "';"
                            + "document.getElementsByTagName('form')[0].submit();";
                    webEngine.executeScript(js);
                }
            }
        });

        webView.setPrefWidth(300);
        webView.setPrefWidth(500);
        webView.setVisible(true);
    }
}
