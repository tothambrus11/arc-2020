package hu.johetajava;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_RECEIVED;

public class Prizm {
    private final String portName;
    private final SerialPort port;
    private final Scanner input;
    private final PrintWriter output;

    private List<MotorPositionsListener> positionsListeners = new ArrayList<>();
    private List<BatteryVoltageListener> batteryVoltageListeners = new ArrayList<>();
    float[] motorPositions = new float[4];
    float batteryVoltage = 15;

    Prizm(String portName) {
        this.portName = portName;
        this.port = SerialPort.getCommPort(portName);
        port.setBaudRate(115200);
        port.openPort();

        this.input = new Scanner(port.getInputStream());
        this.output = new PrintWriter(port.getOutputStream());

        port.addDataListener(new SerialPortMessageListener() {
            @Override
            public int getListeningEvents() {
                return LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                String[] b = new String(serialPortEvent.getReceivedData()).split(" ");

                for (int i = 0; i < 4; i++) {
                    //motorPositions[i] = Float.parseFloat(b[i]);
                }
                emitPositionData(motorPositions);

                batteryVoltage = Float.parseFloat(b[0]);
                emitBatteryVoltage(batteryVoltage);
            }

            @Override
            public byte[] getMessageDelimiter() {
                return "\n".getBytes();
            }

            @Override
            public boolean delimiterIndicatesEndOfMessage() {
                return true;
            }

        });
    }

    void resetEncoders() {
        output.println("1 -1");
    }

    void setMotorSpeed(short motorID, float speed) {
        output.printf("0 %x %f%n", motorID, speed);
    }

    void addPositionDataListener(MotorPositionsListener listener) {
        positionsListeners.add(listener);
    }

    void addBatteryVoltageListener(BatteryVoltageListener listener) {
        batteryVoltageListeners.add(listener);
    }

    void removePositionDataListener(MotorPositionsListener listener) {
        positionsListeners.remove(listener);
    }

    void removeBatteryVoltageListener(BatteryVoltageListener listener) {
        batteryVoltageListeners.remove(listener);
    }

    void emitPositionData(float[] data) {
        positionsListeners.forEach(listener -> listener.onData(data));
    }

    void emitBatteryVoltage(float voltage) {
        batteryVoltageListeners.forEach(listener -> listener.onData(voltage));
    }
}

interface MotorPositionsListener {
    void onData(float[] positions);
}

interface BatteryVoltageListener {
    void onData(float voltage);
}
