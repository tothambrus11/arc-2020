package hu.johetajava;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class QRCodeScanner {
    public static String[] decodeQRCodes(BufferedImage wholeImage) throws IOException {

        BufferedImage[] images = cropImage(wholeImage);

        ArrayList<String> codes = new ArrayList<>();

        for (int i = 0; i < images.length; i++) {
            BufferedImage image = images[i];

            ImageIO.write(image, "png", new File("image-" + i + ".png"));

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            HashMap<DecodeHintType, Boolean> hints = new HashMap<>();
            hints.put(DecodeHintType.TRY_HARDER, true);

            try {
                Result[] result = new QRCodeMultiReader().decodeMultiple(bitmap, hints);
                if (result.length >= 1) {
                    codes.add(result[0].getText());
                } else {
                    System.out.println(i);
                }
            } catch (NotFoundException e) {
                System.out.println(i);
            }
        }

        return codes.toArray(new String[0]);
    }

    public static String[] decodeQRCodesNoCrop(BufferedImage image) throws IOException {
        ArrayList<String> codes = new ArrayList<>();

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        HashMap<DecodeHintType, Boolean> hints = new HashMap<>();
        hints.put(DecodeHintType.TRY_HARDER, true);

        try {
            Result[] result = new QRCodeMultiReader().decodeMultiple(bitmap, hints);

            System.out.println("RES SIE" + result.length);
            for (Result res : result) {
                codes.add(res.getText());
            }
        } catch (NotFoundException e) {
            System.err.println(e);
        }


        return codes.toArray(new String[0]);
    }

    public static BufferedImage[] cropImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int yOffset = 100;
        int topStart = 200;
        int leftStart = 150;
        return new BufferedImage[]{
                image.getSubimage(w / 4 + leftStart, yOffset + topStart, w / 4 - leftStart, h / 2 - topStart),
                image.getSubimage(w / 2, yOffset + topStart, w / 4 - 150, h / 2 - topStart),
                image.getSubimage(w / 4 + leftStart, h / 2 + yOffset, w / 4 - leftStart, h / 2 - yOffset - 100),
                image.getSubimage(w / 2, h / 2 + yOffset, w / 4 - 150, h / 2 - yOffset - 100)
        };
    }
}
