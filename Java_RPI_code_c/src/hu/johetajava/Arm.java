package hu.johetajava;

public class Arm {
    Prizm prizm;

    public static final byte TOPIC_ARM_LEFT = 10;
    public static final byte TOPIC_ARM_RIGHT = 11;
    public static final byte TOPIC_ARM_UP = 12;
    public static final byte TOPIC_ARM_POS = 13;
    public static final double ARM_EC_PER_MM = 1100d / 47; // 47mm volt 1100 encoder count

    public int posOffset = 0;

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
            sleep(1000);
        }
    }

    void armUp(int angle) {
        prizm.sendMessage(TOPIC_ARM_UP, new byte[]{(byte) angle});
    }

    void up(boolean wait) {
        armUp(70);
        if (wait) sleep(500);
    }

    void down(boolean wait) {
        armUp(130);
        if (wait) sleep(500);
    }

    void catchCubeRight() {
        Main.chassis.go(-0.06f, 20, true);
        closeLeft(false);
        setPos(-1100f, true);
        down(true);
        Main.chassis.goToEdge(20);
        Main.chassis.go(0.06f, 20, true);

        closeRight(true);
        up(true);
        reset();
    }

    void catchCubeLeft(){
        Main.chassis.go(-0.06f, 20, true);
        closeLeft(false);
        setPos(1100f, true);
        down(true);
        Main.chassis.goToEdge(20);
        Main.chassis.go(0.06f, 20, true);

        closeLeft(true);
        up(true);

        reset();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void setPos(Float pos, boolean wait) {
        pos += posOffset;

        prizm.sendMessage(TOPIC_ARM_POS, pos.toString().getBytes());
        //prizm.send( (byte) (wait ? 1 : 0));
        if (wait) {
            prizm.waitForOk();
            System.out.println("OK received");
        }
    }

    int mmToArmOffset(double mmValue) {
        return (int) (mmValue * ARM_EC_PER_MM);
    }

    public void reset() {
        posOffset = 0;
        setPos(0f, true);
    }
}
