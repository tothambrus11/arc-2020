package hu.johetajava;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import java.util.Scanner;

public class Prizm {
    public final SerialPort serialPort;
    public static final byte OK_RESPONSE = 0x11;
    public static final byte TOPIC_LED_STATE = 4;
    public static final byte TOPIC_WAIT_FOR_LEFT_BUTTON_PRESSED = 5;
    public static final byte TOPIC_WAIT_FOR_RIGHT_BUTTON_PRESSED = 6;

    public Scanner scanner;

    Prizm(String portName, int port) {
        this.serialPort = SerialPort.getCommPort(portName);

        serialPort.setBaudRate(port);
        if (!serialPort.openPort()) {
            throw new SerialPortInvalidPortException("Failed to connect to the PRIZM");
        }
        scanner = new Scanner(serialPort.getInputStream());

    }

    public void sendMessage(int topic, byte[] message) {
        sendMessage((byte) topic, message);
    }

    public void sendMessage(byte topic, byte[] message) {
        serialPort.writeBytes(new byte[]{topic}, 1);
        serialPort.writeBytes(message, message.length);
    }

    public void send(byte... data) {
        serialPort.writeBytes(data, data.length);
    }

    private static void reverse(byte[] data) {
        int left = 0;
        int right = data.length - 1;

        while (left < right) {
            // swap the values at the left and right indices
            byte temp = data[left];
            data[left] = data[right];
            data[right] = temp;

            // move the left and right index pointers in toward the center
            left++;
            right--;
        }
    }

    public void waitForOk() {
        if (readByte() == OK_RESPONSE) return;
        waitForOk();
    }

    public void waitForOk(int checkTime) {
        if (readByte() == OK_RESPONSE) return;
        try {
            Thread.sleep(checkTime);
        } catch (InterruptedException ignored) {
        }
        waitForOk();
    }

    public void setLEDState(boolean on) {
        send(TOPIC_LED_STATE, (byte) (on ? 100 : 0));
        System.out.println("LED " + (on ? "on" : "off"));
    }

    public void waitForLeftButtonPressed() {
        send(TOPIC_WAIT_FOR_LEFT_BUTTON_PRESSED);
        waitForOk();
    }

    public void waitForRightButtonPressed() {
        send(TOPIC_WAIT_FOR_RIGHT_BUTTON_PRESSED);
        waitForOk();
    }

    public boolean available() {
        return serialPort.bytesAvailable() > 0;
    }

    public byte readByte() {
        while (!available()) ;
        byte[] buffer = new byte[1];
        serialPort.readBytes(buffer, 1);
        return buffer[0];
    }
}
