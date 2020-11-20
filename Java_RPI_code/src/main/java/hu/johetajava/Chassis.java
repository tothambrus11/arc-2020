package hu.johetajava;

public class Chassis {
    Prizm prizm;

    public static final byte TOPIC_STOP = 0;
    public static final byte TOPIC_SET_SPEED_FORWARD = 1;
    public static final byte TOPIC_TURN_ROTATIONS = 2;
    public static final byte TOPIC_GO_FORWARD_UNITS = 3;
    public static final byte TOPIC_STOP_LEFT = 7;
    public static final byte TOPIC_STOP_RIGHT = 8;
    public static final byte TOPIC_WAIT_FOR_ANY_BUTTON_PRESSED = 9;


    Chassis(Prizm prizm) {
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
        prizm.send((byte) ' ', (byte) speed, (byte) (wait ? 0 : 1));
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
        prizm.send((byte) ' ', (byte) speed, (byte) (wait ? 0 : 1));
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


}
