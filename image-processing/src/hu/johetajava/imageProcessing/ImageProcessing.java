package hu.johetajava.imageProcessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ImageProcessing {
    public static final int CUBE_IN_PX = 320;
    private static final int BRIGHTNESS_THRESHOLD = 220;
    public static final double D_MM = 260;

    public static final int UNIT_IN_MM = 115;
    public static final int CUBE_IN_MM = 50;
    public static final double UNIT_PER_PIXEL = (double) CUBE_IN_MM / (UNIT_IN_MM * CUBE_IN_PX);
    public static final double D_IN_UNITS = D_MM / UNIT_IN_MM;

    private static boolean isDebug = false;

    public static void main(String[] args) throws IOException, NoCubeFoundException {
        isDebug = false;
        File f = new File("ynl.jpg");
        var img = ImageIO.read(f);
        CubePosInfo info = cubeInfoOnPicture(img);
    }

    public static double pxToUnit(double px) {
        return px * UNIT_PER_PIXEL;
    }

    public static double pxToMM(int px) {
        return px * CUBE_IN_MM / (double) CUBE_IN_PX;
    }

    public static double unitToMM(double unit) {
        return UNIT_IN_MM * unit;
    }


    public static CubePosInfo cubeInfoOnPicture(BufferedImage img) throws NoCubeFoundException {
        System.out.println("\n  CUBE");
        var cubeEntry = getColorBestEntry(img.getHeight() / 2, img);
        System.out.println("      === "+cubeEntry.getKey());
        System.out.println("\n  BOX");
        var boxEntry = getColorBestEntry(img.getHeight() - 1, img);

        double posPx = (double) cubeEntry.getValue().pixelSum / (double) cubeEntry.getValue().pixelCount;
        double posErrorPx = posPx - img.getWidth() / 2d;

        if (cubeEntry.getKey() == MyColor.NONE || Math.abs(cubeEntry.getValue().pixelCount - CUBE_IN_PX) < 0.3 * CUBE_IN_PX) { // Rossz kép, próbáljuk újra...
            throw new NoCubeFoundException();
        }
        return new CubePosInfo(
                pxToUnit(posErrorPx),// error [units]
                cubeEntry.getKey(),  // cube color
                boxEntry.getKey()    // box color
        );
    }

    static Entry<MyColor, ColorInfo> getColorBestEntry(int y, BufferedImage img) throws NoCubeFoundException {
        HashMap<MyColor, ColorInfo> cubeColorInfos = new HashMap<>();
        cubeColorInfos.put(MyColor.RED, new ColorInfo());
        cubeColorInfos.put(MyColor.YELLOW, new ColorInfo());
        cubeColorInfos.put(MyColor.ORANGE, new ColorInfo());
        cubeColorInfos.put(MyColor.BLUE, new ColorInfo());
        cubeColorInfos.put(MyColor.GREEN, new ColorInfo());
        cubeColorInfos.put(MyColor.NONE, new ColorInfo());

        float[] hsb = new float[3];
        Color color;
        MyColor mc;


        for (int x = 0; x < img.getWidth(); x++) {
            color = new Color(img.getRGB(x, y));

            if (isDebug) System.out.print("\n" + x);
            mc = getMyColor(color);
            if (isDebug) System.out.print(mc);
            if (mc != MyColor.NONE) {

                cubeColorInfos.get(mc).pixelCount++;
                cubeColorInfos.get(mc).pixelSum += x;
            }
        }

        var es = cubeColorInfos.entrySet();
        Entry<MyColor, ColorInfo> bestEntry = new Entry<>() {
            @Override
            public MyColor getKey() {
                return MyColor.NONE;
            }

            @Override
            public ColorInfo getValue() {
                return new ColorInfo(0, 0);
            }

            @Override
            public ColorInfo setValue(ColorInfo colorInfo) {
                return null;
            }
        };

        for (Entry<MyColor, ColorInfo> entry : es) {
            if (entry.getValue().pixelCount > bestEntry.getValue().pixelCount) {
                bestEntry = entry;
            }
            if (entry.getValue().pixelCount > 0) {
                System.out.println(entry.getKey() + " -> " + entry.getValue().pixelCount);
            }
        }

        return bestEntry;
    }

    static MyColor getMyColor(Color color) {
        float[] hsb = new float[3];

        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        double hue = hsb[0] * 360;
        double saturation = hsb[1];
        double brightness = hsb[2];

        if (isDebug) {
            System.out.print("    \t  " + (hue / 360 * 240) + "; " + saturation * 240 + "; " + brightness * 240 + "; -- " + hue + "; " + saturation + "; " + brightness);
        }
        if (brightness <= 1 && brightness > 0.2) {
            if (hue >= 315 && hue < 349 && saturation >= 0.2 && brightness > 0.3) {
                return MyColor.RED;
            } else if (((hue >= 0 && hue < 45) || hue >= 349) && saturation >= 0.45 && brightness > 0.32) {
                return MyColor.ORANGE;
            } else if (hue >= 45 && hue < 80 && saturation > 0.5 && brightness > 0.5) {
                return MyColor.YELLOW;
            } else if (hue >= 80 && hue < 185 && saturation >= 0.2) {
                return MyColor.GREEN;
            } else if (hue >= 210 && hue < 270 && saturation > 0.25 && brightness > 0.32) {
                return MyColor.BLUE;
            }
        }
        return MyColor.NONE;
    }

    public static String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Calendar.getInstance().getTime());
    }
}


