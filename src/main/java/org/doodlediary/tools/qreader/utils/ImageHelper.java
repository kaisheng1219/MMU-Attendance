package org.doodlediary.tools.qreader.utils;

import javafx.scene.image.*;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.nio.IntBuffer;

public class ImageHelper {
    public static WritableImage convertFromBufferedToWritableImage(BufferedImage bufferedImage) {
        WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
            }
        }

        return writableImage;
    }

    public static BufferedImage convertFromWritableToBufferedImage(WritableImage writableImage) {
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = bufferedImage.getRaster();
        int[] pixels = new int[width];

        PixelReader pixelReader = writableImage.getPixelReader();
        WritablePixelFormat<IntBuffer> format = PixelFormat.getIntArgbPreInstance();

        for (int y = 0; y < height; y++) {
            pixelReader.getPixels(0, y, width, 1, format, pixels, 0, width);
            raster.setDataElements(0, y, width, 1, pixels);
        }

        return bufferedImage;
    }
}
