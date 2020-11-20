package hu.johetajava.prizm;

import com.fazecast.jSerialComm.SerialPort;
import com.pi4j.io.gpio.RaspiPin;
import hu.johetajava.Main;
import hu.johetajava.prizm.Encoder;
import hu.johetajava.prizm.Motor;

import java.io.PrintWriter;

public class MotorController {
    private SerialPort serialPort;
    private PrintWriter pw;

    Motor A, B, C, D;

    byte[] speeds = {100, 100, 100, 100};

    MotorController(String port, int baudRate) throws Exception {
        serialPort = SerialPort.getCommPort(port);
        serialPort.setBaudRate(baudRate);
        serialPort.openPort();
        if (serialPort.isOpen()){
            System.out.println("Connection was successful.");
        }
        else {
            System.out.println("Connection failed");
            throw new Exception("Connection failed");
        }

        pw = new PrintWriter(serialPort.getOutputStream());

        A = new Motor(new Encoder(RaspiPin.GPIO_00, RaspiPin.GPIO_02), 0);
        B = new Motor(new Encoder(RaspiPin.GPIO_03, RaspiPin.GPIO_04), 1);
        C = new Motor(new Encoder(RaspiPin.GPIO_05, RaspiPin.GPIO_06), 2);
        D = new Motor(new Encoder(RaspiPin.GPIO_21, RaspiPin.GPIO_27), 3);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int deltaTime = 20;
    int previousPos = 0;

    float integral;
    float targetSpeed = 400;

    double value;
    double prevError = 0;

    double derivative;

    double error;

    //double P = 1, I = 1.8, D_ = 0.04;

    void loop() throws InterruptedException {
        /*int currentPos = A.encoder.value();
        double currentSpeed = ((double) (currentPos - previousPos)) / ((double) deltaTime / 1000d);
        double error = targetSpeed - currentSpeed;

        integral += error * 0.05;
        //integral *= 0.999;
        integral = Math.max(Math.min(integral, 100), -100);

        derivative = (error - prevError)*0.1;
        System.out.println("Error: " + error + ";\t Current speed: " + currentSpeed + "\t current pos: " + A.encoder.value() + "\t prev pos: " + previousPos + "\t I: " + integral + "\t P: " + (error * 0.06) + "\t D: " + derivative);

        value = integral + derivative * 1.1;
        value = Math.min(100, Math.max(-100, value));
        byte[] a = (value+" 0 0 0\n").getBytes();

        //System.out.println("0 0 0 " + value);
        serialPort.writeBytes(a, a.length);

        previousPos = currentPos;
        prevError = error;*/
        error = A.targetPosition - A.encoder.value();
        //System.out.print("error: " + error);


        integral += error * deltaTime / 1000;
        integral = (float) Math.min(Math.max(integral, -Main.args[3]), Main.args[3]);

        //System.out.print("\t integral: " + (integral * Main.args[1]));

        derivative = (error - prevError) / (deltaTime / 1000d);
        //System.out.print("\t derivative: " + (derivative * Main.args[2]));

        if(Math.abs(error) <= 1){
            integral = 0;
            derivative = 0;
        }
        if (error == 0 && derivative == 0) {
            integral = 0;
        }
        double ASpeed = error * Main.args[0] + integral * Main.args[1] + derivative * Main.args[2];
        //System.out.print("\t speed: " + ASpeed + "\n");

        ASpeed = Math.min(100, Math.max(ASpeed, -100));

        speeds[0] = (byte) (ASpeed + 100);

        setSpeed();
        prevError = error;
    }

    public void setSpeed() {
        serialPort.writeBytes(speeds, 4);
    }

    public void stopMotors() {
        serialPort.writeBytes(new byte[]{100, 100, 100, 100}, 4);
    }
}

