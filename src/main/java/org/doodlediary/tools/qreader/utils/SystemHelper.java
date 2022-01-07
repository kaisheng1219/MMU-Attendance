package org.doodlediary.tools.qreader.utils;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class SystemHelper {
    public static void copyToClipboard(String content) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(content);
        clipboard.setContent(clipboardContent);
    }
}