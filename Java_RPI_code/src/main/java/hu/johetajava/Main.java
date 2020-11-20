package hu.johetajava;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.AWB;
import com.hopding.jrpicam.enums.Encoding;
import com.pi4j.io.gpio.GpioController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static GpioController gpio;
    public static RPiCamera piCamera;

    private static boolean dir;

    public static double[] args;

    public static boolean stopped = false;
    private static Chassis chassis;
    private static Prizm prizm;
    private static Arm arm;

    // Args [port]
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        //gpio = GpioFactory.getInstance();

        prizm = new Prizm(args[0], 57600);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                stopped = true;
                Thread.sleep(200);
                System.out.println("Shutting down ...");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }));

        Thread.sleep(3000);

        piCamera = new RPiCamera("/home/pi/").setTimeout(1);

        chassis = new Chassis(prizm);
        arm = new Arm(prizm);

        chassis.goToEdge(60);

        while (true) {
            arm.closeLeft(false);
            Thread.sleep(600);
            arm.closeRight(false);
            Thread.sleep(600);
        }
        //prizm.setLEDState(false);

    }

    static void onStart() throws InterruptedException, IOException {
        chassis.goToEdge(100);
        Thread.sleep(200);
        chassis.go(-0.1f, 60, true);
        Thread.sleep(200);

        chassis.goToEdge(30);

        prizm.setLEDState(true);
        chassis.go(-1.6666f, 100, true);

        while (true) {
            BufferedImage image = ImageIO.read(piCamera.takeStill("a.jpg", 3280 / 3, 2464 / 3));

            var res = QRCodeScanner.decodeQRCodesNoCrop(image);

            System.out.println(Arrays.toString(res));
            if (res.length == 4) break;
        }
    }

    static void saveImage(BufferedImage image, String fileName) {
        try {
            ImageIO.write(image, "png", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage average(BufferedImage[] images) {

        int n = images.length;

        // Assuming that all images have the same dimensions
        int w = images[0].getWidth();
        int h = images[0].getHeight();


        BufferedImage average =
                new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);

        WritableRaster raster =
                average.getRaster().createCompatibleWritableRaster();

        for (int y = 0; y < h; ++y)
            for (int x = 0; x < w; ++x) {

                float sum = 0.0f;

                for (int i = 0; i < n; ++i)
                    sum = sum + images[i].getRaster().getSample(x, y, 0);

                raster.setSample(x, y, 0, Math.round(sum / n));
            }

        average.setData(raster);

        return average;
    }
}

class Key {
}

class Data {

}
