import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessing {
    public static final int UNIT_IN_MM = 115;
    public static final int CUBE_IN_MM = 50;
    public static final int CUBE_IN_PX = 178;
    public static final double UNIT_PER_PIXEL = (double) CUBE_IN_MM / (double) UNIT_IN_MM / (double) CUBE_IN_PX;
    public static final double D_MM = 260;
    public static final double D_IN_UNITS = D_MM / UNIT_IN_MM;


    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println(pxToUnit(13));
        Thread.sleep(1000000);
        BufferedImage img = ImageIO.read(new File("pic.jpg"));
        int currentPos = cubePosOnPicture(120, img);
        int desiredPos = img.getHeight() / 2;
        int move = desiredPos - currentPos;
        double unitsToMove = pxToUnit(move);
        double alfa = alfa(unitsToMove);
        double i = alfa * D_IN_UNITS;
        System.out.println("D in units: " + D_IN_UNITS);

        System.out.println(alfa);
    }

    static double alfa(double moveUnits) {
        if (moveUnits < 0) moveUnits = -moveUnits;
        return Math.acos((D_IN_UNITS - moveUnits) / D_IN_UNITS);
    }

    static double pxToUnit(int px) {
        return px * UNIT_PER_PIXEL;
    }

    static int cubePosOnPicture(int threshold, BufferedImage img) {
        int atX = img.getWidth() / 2;

        int sum = 0;
        int sumBr = 0;

        for (int i = 0, br; i < img.getHeight(); i++) {
            int c = img.getRGB(atX, i);
            Color color = new Color(c);
            br = color.getRed() + color.getBlue() + color.getGreen();
            if (br > threshold) {
                sum += br * i;
                sumBr += br;
            }
        }

        return sum / sumBr;
    }

}
