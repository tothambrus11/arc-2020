package hu.johetajava;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static hu.johetajava.Main.piCamera;

public class RobotCamera{

    Prizm prizm;

    public RobotCamera(Prizm prizm) {
        this.prizm = prizm;
    }

    public BufferedImage takePicture() throws IOException, InterruptedException {
        prizm.setLEDState(true);
        File f = piCamera.takeStill("pictures/" + date() + ".jpg", 3280 / 3, 2464 / 3);
        prizm.setLEDState(false);
        return ImageIO.read(f);
    }

    public static String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Calendar.getInstance().getTime());
    }
}
