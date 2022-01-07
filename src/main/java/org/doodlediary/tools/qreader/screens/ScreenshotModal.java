package org.doodlediary.tools.qreader.screens;

import com.google.zxing.NotFoundException;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.doodlediary.tools.qreader.components.ExceptionDialog;
import org.doodlediary.tools.qreader.components.RoundCornerImageView;
import org.doodlediary.tools.qreader.components.SelectableSectionGroup;
import org.doodlediary.tools.qreader.utils.ImageHelper;
import org.doodlediary.tools.qreader.utils.QRReader;
import org.doodlediary.tools.qreader.utils.Screenshoter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScreenshotModal {
    private static String url;

    public static String display() throws AWTException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);

        SelectableSectionGroup root = new SelectableSectionGroup();

        // take a screenshot and place it in a custom image view
        BufferedImage screenshot = Screenshoter.takeScreenshot();
        Image image = ImageHelper.convertFromBufferedToWritableImage(screenshot);
        ImageView imageView = new RoundCornerImageView(image, 50);
        imageView.setEffect(new DropShadow(20, Color.LIGHTBLUE));

        // add right click action
        ContextMenu contextMenu = new ContextMenu();
        MenuItem miDestroy = new MenuItem("Destroy");
        miDestroy.setOnAction(actionEvent -> {
            stage.close();
        });
        MenuItem miRead = new MenuItem("Read");
        miRead.setOnAction(actionEvent -> {
            try {
                readImage(root.getSelectionBounds(), imageView);
                stage.close();
            } catch (NotFoundException e) {
                new ExceptionDialog(e).showAndWait();
                stage.close();
            }
        });
        contextMenu.getItems().addAll(miRead, miDestroy);

        // attach context menu to a label
        Label label = new Label();
        label.setContextMenu(contextMenu);
        label.setMinWidth(imageView.getFitWidth());
        label.setMinHeight(imageView.getFitHeight());

        root.getChildren().addAll(imageView, label);

        root.setScaleX(0.9);
        root.setScaleY(0.9);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.showAndWait();
        return url;
    }

    private static void readImage(Bounds bounds, ImageView imageView) throws NotFoundException {
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));
        WritableImage writableImage = new WritableImage(width, height);
        imageView.snapshot(parameters, writableImage);
        url = QRReader.readQRCode(ImageHelper.convertFromWritableToBufferedImage(writableImage));
    }
}
