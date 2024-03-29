package hu.johetajava;

import hu.johetajava.imageProcessing.CubePosInfo;
import hu.johetajava.imageProcessing.ImageProcessing;
import hu.johetajava.imageProcessing.NoCubeFoundException;

import java.io.IOException;

import static hu.johetajava.Main.*;

public class Chassis {
    hu.johetajava.Prizm prizm;

    public static final byte TOPIC_STOP = 0;
    public static final byte TOPIC_SET_SPEED_FORWARD = 1;
    public static final byte TOPIC_TURN_ROTATIONS = 2;
    public static final byte TOPIC_GO_FORWARD_UNITS = 3;
    public static final byte TOPIC_STOP_LEFT = 7;
    public static final byte TOPIC_STOP_RIGHT = 8;
    public static final byte TOPIC_WAIT_FOR_ANY_BUTTON_PRESSED = 9;
    public static final byte TOPIC_GO_UNITS_LEFT = 14;
    public static final byte TOPIC_GO_UNITS_RIGHT = 15;

    public static final int UNIT_IN_MM = 115;
    public static final double D_MM = 266; // Kikíséretezni, mi a pont jó. Ez elvileg a két kerék távolsága
    public static final double D_IN_UNITS = D_MM / UNIT_IN_MM;

    Chassis(hu.johetajava.Prizm prizm) {
        this.prizm = prizm;
    }

    void setSpeedForward(int speed) {
        prizm.sendMessage(TOPIC_SET_SPEED_FORWARD, new byte[]{(byte) speed});
    }

    void stop() {
        prizm.sendMessage(TOPIC_STOP, new byte[0]);
    }

    /**
     * Rotate the robot
     *
     * @param rotations + values->CCW
     * @param speed
     * @param wait      Wait for motors
     */
    void turnRotations(Float rotations, int speed, boolean wait) {
        if (rotations > 0) {
            play("balra");
        } else if (rotations < 0) {
            play("jobbra");
        }
        prizm.sendMessage(TOPIC_TURN_ROTATIONS, rotations.toString().getBytes());
        prizm.send((byte) ' ', (byte) speed, (byte) (wait ? 1 : 0));
        if (wait) {
            prizm.waitForOk();
        }
    }

    void stopLeft() {
        prizm.send(TOPIC_STOP_LEFT);
    }

    void stopRight() {
        prizm.send(TOPIC_STOP_RIGHT);
    }

    void go(Float units, int speed, boolean wait) {
        prizm.sendMessage(TOPIC_GO_FORWARD_UNITS, units.toString().getBytes());
        prizm.send((byte) ' ', (byte) speed, (byte) (wait ? 1 : 0));
        if (wait) {
            prizm.waitForOk();
        }
    }

    void goLeft(Float units, int speed, boolean wait) {
        prizm.sendMessage(TOPIC_GO_UNITS_LEFT, units.toString().getBytes());
        prizm.send((byte) ' ', (byte) speed, (byte) (wait ? 1 : 0));
        if (wait) {
            prizm.waitForOk();
        }
    }

    void goRight(Float units, int speed, boolean wait) {
        prizm.sendMessage(TOPIC_GO_UNITS_RIGHT, units.toString().getBytes());
        prizm.send((byte) ' ', (byte) speed, (byte) (wait ? 1 : 0));
        if (wait) {
            prizm.waitForOk();
        }
    }

    void goToEdge(int speed) {
        setSpeedForward(speed);

        prizm.send(TOPIC_WAIT_FOR_ANY_BUTTON_PRESSED);
        byte leftIsPressed = prizm.readByte();

        if (leftIsPressed == 0x12) {
            stopLeft();
            prizm.waitForRightButtonPressed();
            stopRight();
        } else if (leftIsPressed == 0x13) {
            stopRight();
            prizm.waitForLeftButtonPressed();
            stopLeft();
        }
        System.out.println("Edge reached.");
    }

    void goToEdgePrecise() {
        goToEdge(40);
        go(-0.1f, 40, true);
        goToEdge(40);
        go(-0.1f, 20, true);
        goToEdge(20);
    }

    void moveSideways(double moveUnits, int speed) {
        System.out.println("Move MMs: " + Math.round(ImageProcessing.unitToMM(moveUnits) * 1000) / 1000d);
        double a = Math.abs(moveUnits) / 2; // Kétszer csináljuk meg a fordulgatást, egyszer hátra, majd mégegyszer előre.
        double alfa = Math.acos((D_IN_UNITS - a) / D_IN_UNITS);
        float i = (float) (D_IN_UNITS * alfa);
        if (moveUnits < 0) {
            // move left
            goRight(-i, speed, true);
            goLeft(-i, speed, true);
            goRight(i, speed, true);
            goLeft(i, speed, true);
        } else {
            // move right
            goLeft(-i, speed, true);
            goRight(-i, speed, true);
            goLeft(i, speed, true);
            goRight(i, speed, true);
        }
    }


    public CubePosInfo positionSidewaysFullProcedure() {
        chassis.goToEdgePrecise();
        go(-0.9f, 100, true);

        CubePosInfo info = robotCamera.getRobotInfoTryHard();

        double mmError = ImageProcessing.unitToMM(info.errorUnits);

        while (Math.abs(mmError) >= 10) {
            System.out.println("Move sideways.");
            chassis.moveSideways(info.errorUnits, 40);

            info = robotCamera.getRobotInfoTryHard();

            mmError = ImageProcessing.unitToMM(info.errorUnits);
        }

        System.out.println("CUBE COLOR: " + info.cubeColor);
        System.out.println("BOX COLOR: " + info.boxColor);

        System.out.println("MM ERROR: " + mmError);
        arm.posOffset = arm.mmToArmOffset(mmError);

        return info;
    }
}
