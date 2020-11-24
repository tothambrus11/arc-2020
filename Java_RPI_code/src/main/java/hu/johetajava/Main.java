package hu.johetajava;

import com.hopding.jrpicam.RPiCamera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static RPiCamera piCamera;

    private static boolean dir;

    public static double[] args;

    public static boolean stopped = false;
    static Chassis chassis;
    static Prizm prizm;
    static Arm arm;

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
        int elt = Integer.parseInt(args[1]);
        if (elt != 0) {
            arm.setPos((float) elt, true);
            return;
        }

        //chassis.turnRotations((float) Float.parseFloat(args[2]), 110, true);

        chassis.goToEdgePrecise();
        chassis.go(-0.9f, 100, true);
        prizm.setLEDState(true);
        Thread.sleep(500);

        double unitError = ImageProcessing.cubePosErrorUnit();
        double mmError = ImageProcessing.unitToMM(unitError);

        if (mmError >= 10) {
            chassis.moveSideways(unitError, 40);
            arm.posOffset = arm.mmToArmOffset(ImageProcessing.unitToMM(ImageProcessing.cubePosErrorUnit()));
        } else {
            arm.posOffset = arm.mmToArmOffset(mmError);
        }

        arm.catchCubeRight();

        chassis.go(-1f, 40, true);

        Thread.sleep(500);
        chassis.turnRotations(-0.5f * Float.parseFloat(args[2]), 100, true);
        System.out.println("TR finished");
        Thread.sleep(500);

        chassis.go(2f, 80, true);
        prizm.setLEDState(true);
        chassis.goToEdgePrecise();
        System.out.println("goToEdge finished");
        piCamera.takeStill("pic.jpg", 3280 / 3, 2464 / 3);
        prizm.setLEDState(false);

        arm.setPos(1100f, true);
        chassis.go(-0.06f, 20, true);
        arm.down(true);
        Main.chassis.goToEdge(20);
        Main.chassis.go(0.06f, 20, true);

        arm.closeLeft(true);
        arm.setPos(-1250f, true);
        Main.chassis.go(-0.06f, 20, true);
        arm.up(true);

        arm.setPos(0f, true);

        /*arm.setPos(1100f, true);
        arm.down(true);
        arm.closeLeft(true);
        arm.up(true);
        arm.setPos(0f, true);
        arm.setPos(1100f, true);
        arm.down(true);
        arm.closeRight(true);
        arm.setPos((float) elt, true);
        arm.up(true);
        arm.setPos(0f, true);


        arm.closeLeft(true);
        arm.setPos(-1100f, true);
        arm.down(true);
        arm.closeRight(true);
        arm.up(true);
        arm.setPos(0f, true);
        arm.setPos(-1100f, true);bran
        arm.down(true);
        arm.closeLeft(true);
        arm.setPos((float) -elt, true);
        arm.up(true);
        arm.setPos(0f, true);
*/
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
