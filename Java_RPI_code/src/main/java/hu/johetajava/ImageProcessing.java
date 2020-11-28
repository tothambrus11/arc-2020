package hu.johetajava;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static hu.johetajava.Main.piCamera;

public class ImageProcessing {
    public static final int CUBE_IN_PX = 325;
    private static final int BRIGHTNESS_THRESHOLD = 220;
    public static final double D_MM = 260;

    public static final int UNIT_IN_MM = 115;
    public static final int CUBE_IN_MM = 50;
    public static final double UNIT_PER_PIXEL = (double) CUBE_IN_MM / (UNIT_IN_MM * CUBE_IN_PX);
    public static final double D_IN_UNITS = D_MM / UNIT_IN_MM;


    static double pxToUnit(int px) {
        return px * UNIT_PER_PIXEL;
    }

    static double pxToMM(int px) {
        return px * CUBE_IN_MM / (double) CUBE_IN_PX;
    }

    static double unitToMM(double unit) {
        return UNIT_IN_MM * unit;
    }

    static int cubePosOnPicturePx(BufferedImage img) {
        int atY = img.getHeight() / 2;

        int sum = 0;
        int sumBr = 0;


        for (int i = 0, br; i < img.getWidth(); i++) {
            int c = img.getRGB(i, atY);
            Color color = new Color(c);
            br = color.getRed() + color.getBlue() + color.getGreen();
            if (br > BRIGHTNESS_THRESHOLD) {
                sum += br * i;
                sumBr += br;
            }
        }

        System.out.println("Cube pos on picture: " + ((double) sum / (double) sumBr));
        return (int) ((double) sum / (double) sumBr);
    }

    static int cubePosErrorPx(BufferedImage img) {
        return ImageProcessing.cubePosOnPicturePx(img) - img.getWidth() / 2;
    }

    static int cubePosErrorPx() throws IOException, InterruptedException {
        return cubePosErrorPx(takePicture());
    }

    static double cubePosErrorUnit(BufferedImage img) {
        return pxToUnit(cubePosErrorPx(img));
    }

    static double cubePosErrorUnit() throws IOException, InterruptedException {
        return cubePosErrorUnit(takePicture());
    }

    public static BufferedImage takePicture() throws IOException, InterruptedException {
        File f = piCamera.takeStill("pic.jpg", 3280 / 3, 2464 / 3);
        return ImageIO.read(f);
    }
}
