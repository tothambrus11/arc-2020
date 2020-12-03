package hu.johetajava;

import com.hopding.jrpicam.RPiCamera;
import hu.johetajava.imageProcessing.CubePosInfo;
import hu.johetajava.imageProcessing.NoCubeFoundException;
import hu.johetajava.pathfinding.Main_pathfinding;
import hu.johetajava.pathfinding.RobotInterface;
import javazoom.jl.player.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static RPiCamera piCamera;

    public static boolean stopped = false;
    static Chassis chassis;
    static Prizm prizm;
    static Arm arm;

    static RobotCamera robotCamera;

    static boolean isArmCalibration = false;

    // Args [port] [armOffset]
    public static void main(String[] args) throws Exception, NoCubeFoundException, NoCubeFoundException {
        System.out.println("Starting robot...");

        prizm = new Prizm(args[0], 57600);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (!isArmCalibration) {
                    arm.posOffset = 0;
                    arm.setPos(0f, true);
                }
                stopped = true;
                prizm.serialPort.closePort();
                prizm.serialPort.openPort();
                Thread.sleep(200);
                System.out.println("Shutting down ...");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }));

        Thread.sleep(3000);

        piCamera = new RPiCamera("/home/pi/").setTimeout(400);

        chassis = new Chassis(prizm);
        arm = new Arm(prizm);
        robotCamera = new RobotCamera(prizm);

        int elt = Integer.parseInt(args[1]);
        if (elt != 0) {
            isArmCalibration = true;
            arm.setAbsolutePos((float) elt, true);
            return;
        }




       /* chassis.goToEdgePrecise();

        chassis.positionSidewaysFullProcedure();

        System.exit(0);
*/
        //ImageProcessing.takePicture();
/*
        chassis.positionSidewaysFullProcedure();
        chassis.goToEdgePrecise();
        arm.catchCubeLeft();
        arm.swapCubesToRight();
        Thread.sleep(2000);
        arm.closeLeft(true);
        Thread.sleep(3000);

        arm.catchCubeRight();
        arm.swapCubesToLeft();
*/

        Main_pathfinding.run(new MyRobotInterface());
        System.exit(1);




        onStart();
        chassis.turnRotations(0.5f, 80, true);
        chassis.go(5f, 200, true);

        chassis.turnRotations(0.25f, 80, true);
        chassis.go(1.5f, 100, true);

        chassis.goToEdgePrecise();
        chassis.positionSidewaysFullProcedure();
        chassis.goToEdgePrecise();

        arm.catchCubeRight();

        chassis.go(-1f, 40, true);

        chassis.turnRotations(-0.5f, 100, true);

        chassis.go(2f, 200, true);
        chassis.positionSidewaysFullProcedure();
        chassis.goToEdgePrecise();

        arm.swapCubesToLeft();

    }

    static void play(String fileName) {
        new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(fileName + ".mp3");
                Player playMP3 = new Player(fis);

                playMP3.play();
            } catch (Exception e) {
                System.out.println(e);
            }
        }).start();
    }

    static void onStart() throws InterruptedException, IOException {

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

    static int counter = 0;
    static class MyRobotInterface extends RobotInterface {

        @Override
        public void go(double units) {
            if(counter < 2){
                counter++;
                return;
            }
            System.out.println("[COMMAND] GO _" + units + "_ units");
            chassis.go((float) units, 200, true);
        }

        @Override
        public void turn(double fullRotations) {
            if(counter < 2){
                counter++;
                return;
            }
            System.out.println("[COMMAND] TURN _" + fullRotations + "_ rotations");
            chassis.turnRotations((float) fullRotations, 80, true);
        }

        @Override
        public CubePosInfo positionToCube() {
            System.out.println("[COMMAND] POSITION TO CUBE");
            return chassis.positionSidewaysFullProcedure();
        }

        @Override
        public void goToEdge() {
            System.out.println("[COMMAND] GO TO EDGE");
            chassis.goToEdgePrecise();
        }

        @Override
        public String[] readMaps() {
            System.out.println("[COMMAND] READ MAPS");

            chassis.goToEdgePrecise();

            chassis.goToEdge(30);

            chassis.go(-2.5f, 200, true);

            String[] codes = null;

            do {
                BufferedImage image = null;
                try {
                    image = robotCamera.takePicture();
                    codes = QRCodeScanner.decodeQRCodesNoCrop(image);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Trying...");
            } while (codes == null || codes.length != 4);
            System.out.println(Arrays.deepToString(codes));

            chassis.go(-0.369f, 40, true);

            chassis.turnRotations(0.5f, 80, true);

            chassis.go(.5f, 200, true);
            return codes;
        }

        @Override
        public boolean isTrueBox() {
            return true; // TODO ADD ultrasonic sensor
        }

    }
}
