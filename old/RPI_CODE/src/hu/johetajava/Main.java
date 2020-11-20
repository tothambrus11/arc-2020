package hu.johetajava;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Prizm prizm = new Prizm("COM14");

        MotorPositionsListener listener = positions -> System.out.println(Arrays.toString(positions));

        prizm.addPositionDataListener(listener);

        prizm.addBatteryVoltageListener(voltage -> {
            System.out.printf("Current voltage is %sV.%n", voltage);
        });

    }
}
