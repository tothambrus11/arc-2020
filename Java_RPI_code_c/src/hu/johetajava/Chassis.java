package hu.johetajava;

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
        prizm.sendMessage(TOPIC_TURN_ROTATIONS, rotations.toString().getBytes());
        prizm.send((byte) ' ', (byte) speed, (byte) (wait ? 1 : 0));
        if (wait) {
            System.out.println("TR waiting for ok");
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

        System.out.printf("0x%x ", leftIsPressed);
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
        System.out.println("MoveUnits: " + moveUnits);
        System.out.println("MoveMMs: " + ImageProcessing.unitToMM(moveUnits));
        double a = Math.abs(moveUnits) / 2; // Kétszer csináljuk meg a fordulgatást, egyszer hátra, majd mégegyszer előre.
        double alfa = Math.acos((D_IN_UNITS - a) / D_IN_UNITS);
        System.out.println("alfa: " + alfa);
        float i = (float) (D_IN_UNITS * alfa);
        System.out.println("I=" + i);
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


    public void positionSidewaysFullProcedure() throws InterruptedException, IOException, NoCubeFoundException {
        go(-0.9f, 100, true);

        double unitError = ImageProcessing.cubePosErrorUnit(robotCamera.takePicture());
        double mmError = ImageProcessing.unitToMM(unitError);

        while (Math.abs(mmError) >= 10) {
            System.out.println("Move sideways");
            chassis.moveSideways(unitError, 40);

            unitError = ImageProcessing.cubePosErrorUnit(robotCamera.takePicture());
            mmError = ImageProcessing.unitToMM(unitError);
        }
        System.out.println("MM ERROR: " + mmError);
        arm.posOffset = arm.mmToArmOffset(mmError);
        System.out.println("POS OFFSET: " + arm.posOffset);

    }
}
