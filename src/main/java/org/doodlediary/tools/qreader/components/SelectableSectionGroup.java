package org.doodlediary.tools.qreader.components;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Scale;

public class SelectableSectionGroup extends Group {
    private final DragContext dragContext = new DragContext();
    private Rectangle selectionRect;
    private double xOffset;
    private double yOffset;
    private Scale scale;

    public SelectableSectionGroup() {
        selectionRect = new Rectangle();
        selectionRect.setStroke(Color.TEAL);
        selectionRect.setStrokeWidth(2);
        selectionRect.setStrokeLineCap(StrokeLineCap.ROUND);
        selectionRect.setFill(Color.TEAL.deriveColor(0, 1.2, 1, 0.4));

        addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressed());
        addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDragged());
        addEventHandler(ScrollEvent.SCROLL, onScrollEvent());

        // scale from top left corner
        scale = new Scale();
        getTransforms().add(scale);
    }

    private static final class DragContext {
        double mouseAnchorX;
        double mouseAnchorY;
    }

    private EventHandler<MouseEvent> onMouseDragged() {
        return mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                double offsetX = mouseEvent.getX() - dragContext.mouseAnchorX;
                double offsetY = mouseEvent.getY() - dragContext.mouseAnchorY;

                if (offsetX > 0) selectionRect.setWidth(offsetX);
                else {
                    selectionRect.setX(mouseEvent.getX());
                    selectionRect.setWidth(dragContext.mouseAnchorX - selectionRect.getX());
                }

                if (offsetY > 0) selectionRect.setHeight(offsetY);
                else {
                    selectionRect.setY(mouseEvent.getY());
                    selectionRect.setHeight(dragContext.mouseAnchorY - selectionRect.getY());
                }
            } else if (mouseEvent.isMiddleButtonDown()) {
                // make window draggable
                getScene().getWindow().setX(mouseEvent.getScreenX() - xOffset);
                getScene().getWindow().setY(mouseEvent.getScreenY() - yOffset);
            }
        };
    }

    private EventHandler<MouseEvent> onMousePressed() {
        return mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                // remove old selection
                selectionRect.setX(0);
                selectionRect.setY(0);
                selectionRect.setWidth(0);
                selectionRect.setHeight(0);
                getChildren().remove(selectionRect);

                // create new selection
                dragContext.mouseAnchorX = mouseEvent.getX();
                dragContext.mouseAnchorY = mouseEvent.getY();
                selectionRect.setX(dragContext.mouseAnchorX);
                selectionRect.setY(dragContext.mouseAnchorY);
                getChildren().add(selectionRect);
            } else if (mouseEvent.isMiddleButtonDown()) {
                // make window draggable
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            }
        };
    }

    private EventHandler<ScrollEvent> onScrollEvent() {
        return scrollEvent -> {
            double scroll = scrollEvent.getDeltaY();
            double scaleFactor = scale.getX() + scroll / 100;
            if (scaleFactor >= 0.1 && scaleFactor <= 7) {
                scale.setX(scaleFactor);
                scale.setY(scaleFactor);
            }
        };
    }

    public Bounds getSelectionBounds() {
        return selectionRect.getBoundsInParent();
    }
}
