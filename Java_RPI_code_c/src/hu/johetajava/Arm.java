package hu.johetajava;

import static hu.johetajava.Main.chassis;

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
        Main.play("horukk");
        armUp(70);
        if (wait) sleep(500);
    }

    void down(boolean wait) {
        armUp(130);
        if (wait) sleep(500);
    }

    void catchCubeRight() {
        chassis.go(-0.06f, 20, true);
        closeLeft(false);
        setPos(-1100f, true);
        down(true);
        chassis.goToEdge(20);
        chassis.go(0.06f, 20, true);

        closeRight(true);
        up(true);
        setAbsolutePos(0f, false);
    }

    void catchCubeLeft(){
        chassis.go(-0.06f, 20, true);
        closeRight(false);
        setPos(1100f, true);
        down(true);
        chassis.goToEdge(20);
        chassis.go(0.06f, 20, true);

        closeLeft(true);
        up(true);

        setAbsolutePos(0f, false);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void setPos(Float pos, boolean wait) {
        setAbsolutePos(pos + posOffset, wait);
    }

    int mmToArmOffset(double mmValue) {
        return (int) (mmValue * ARM_EC_PER_MM);
    }

    public void reset() {
        reset(true);
    }

    public void reset(boolean wait){
        posOffset = 0;
        setPos(0f, wait);
    }

    public void setAbsolutePos(Float pos, boolean wait){
        prizm.sendMessage(TOPIC_ARM_POS, pos.toString().getBytes());
        prizm.send((byte) ' ', (byte) (wait ? 1 : 0));
        if (wait) {
            System.out.println("waiting for ok...");
            prizm.waitForOk(50);
            System.out.println("OK received");
        }
    }

    public void swapCubesToLeft() {
        chassis.go(-0.06f, 20, false);
        setPos(1100f, true);
        down(true);
        chassis.goToEdge(20);
        chassis.go(0.06f, 20, true);

        closeLeft(true);
        setPos(-1220f, true);
        chassis.go(-0.06f, 20, true);
        up(true);

        setAbsolutePos(0f, false);
    }

    public void swapCubesToRight() {
        chassis.go(-0.06f, 20, false);
        setPos(-1100f, true);
        down(true);
        chassis.goToEdge(20);
        chassis.go(0.06f, 20, true);

        closeRight(true);
        setPos(1220f, true);
        chassis.go(-0.06f, 20, true);
        up(true);

        setAbsolutePos(0f, false);
    }
}
