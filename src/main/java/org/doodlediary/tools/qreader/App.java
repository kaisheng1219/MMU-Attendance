package org.doodlediary.tools.qreader;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.doodlediary.tools.qreader.screens.MainPage;

import java.util.prefs.BackingStoreException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws BackingStoreException {
        Scene scene = new Scene(new MainPage(stage));
        stage.setTitle("MMU Attendance");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}