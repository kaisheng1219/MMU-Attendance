package org.doodlediary.tools.qreader.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Screenshoter {
    public static BufferedImage takeScreenshot() throws AWTException {
        Robot robot = new Robot();
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(rectangle);
    }

    public static void main(String[] args) throws AWTException {
        System.out.println(Screenshoter.takeScreenshot().getType());
    }
}
