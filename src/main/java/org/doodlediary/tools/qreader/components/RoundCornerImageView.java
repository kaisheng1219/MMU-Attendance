package org.doodlediary.tools.qreader.components;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RoundCornerImageView extends ImageView {

    public RoundCornerImageView(Image image, int radius) {
        super(image);
        setFitWidth(image.getWidth());
        setFitHeight(image.getHeight());

        // clip the image to a rounded rectangle
        Rectangle clip = new Rectangle(getFitWidth(), getFitHeight());
        clip.setArcWidth(radius);
        clip.setArcHeight(radius);
        setClip(clip);

        // snapshot
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        setImage(snapshot(parameters, null));

        // remove the clip
        setClip(null);
    }
}
