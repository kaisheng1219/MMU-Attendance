package org.doodlediary.tools.qreader.components;

import com.google.zxing.NotFoundException;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionDialog extends Alert {
    public ExceptionDialog(Exception e) {
        super(AlertType.ERROR);
        setTitle("Exception Dialog");

        String exceptionString = "";

        if (e instanceof NotFoundException)
            exceptionString = "No QR Code Found! Please ensure that the QR Code is clearly visible.";
        else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            exceptionString = sw.toString();
        }

        Label label = new Label("The exception cause was:");
        TextArea textArea = new TextArea(exceptionString);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        getDialogPane().setExpandableContent(expContent);
        getDialogPane().setExpanded(true);
    }
}
