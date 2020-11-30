package hu.johetajava;

import hu.johetajava.imageProcessing.CubePosInfo;
import hu.johetajava.imageProcessing.ImageProcessing;
import hu.johetajava.imageProcessing.NoCubeFoundException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static hu.johetajava.Main.piCamera;

public class RobotCamera {

    Prizm prizm;

    public RobotCamera(Prizm prizm) {
        this.prizm = prizm;
    }

    public BufferedImage takePicture() throws IOException, InterruptedException {
        return takePicture(true);
    }

    public BufferedImage takePicture(boolean lights) throws IOException, InterruptedException {
        prizm.setLEDState(lights);
        File f = piCamera.takeStill("pictures/" + date() + ".jpg", 3280 / 3, 2464 / 3);
        if (lights) prizm.setLEDState(false);
        return ImageIO.read(f);
    }

    public static String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Calendar.getInstance().getTime());
    }

    public CubePosInfo getRobotInfoTryHard() {
        CubePosInfo info = null;

        boolean lights = true;
        do {
            try {
                System.out.println("<<<<<<<<<<<<<<< WIT L " + lights + "\n");
                info = ImageProcessing.cubeInfoOnPicture(takePicture(lights));
            } catch (NoCubeFoundException | IOException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("\n=== NO CUBE FOUND");
                continue;
            }
            break;
        } while (true);


        return info;
    }
}
