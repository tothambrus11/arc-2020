package hu.johetajava;

public class Arm {
    Prizm prizm;

    public static final byte TOPIC_ARM_LEFT = 10;
    public static final byte TOPIC_ARM_RIGHT = 11;
    public static final byte TOPIC_ARM_UP = 12;

    Arm(Prizm prizm) {
        this.prizm = prizm;
    }

    void closeLeft(boolean wait) {
        prizm.send(TOPIC_ARM_LEFT);
        if (wait) {
            sleep(1000);
        }
    }

    void closeRight(boolean wait) {
        prizm.send(TOPIC_ARM_RIGHT);
        if (wait) {
            sleep(400);
        }
    }

    void armUp(int angle) {
        prizm.sendMessage(TOPIC_ARM_UP, new byte[]{(byte) angle});
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
