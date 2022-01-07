package org.doodlediary.tools.qreader.utils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;

public class QRReader {
    public static String readQRCode(BufferedImage bufferedImage) throws NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(
                new HybridBinarizer(
                        new BufferedImageLuminanceSource(
                                bufferedImage)));
        Result result = new MultiFormatReader().decode(binaryBitmap);
        return result.getText();
    }
}
