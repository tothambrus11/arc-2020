package hu.johetajava.prizm;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public class Prizm {
    private I2CBus i2cBus;

    private I2CDevice motorControllerPrizmChip;
    private I2CDevice servoPrizmChip;

    private I2CDevice expansionController1;
    private I2CDevice expansionController2;
    private I2CDevice expansionController3;
    private I2CDevice expansionController4;

    public void prizmBegin() throws IOException, I2CFactory.UnsupportedBusNumberException {
        i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);

        // Init I2C devices
        motorControllerPrizmChip = i2cBus.getDevice(0x5);
        servoPrizmChip = i2cBus.getDevice(0x6);

        expansionController1 = i2cBus.getDevice(0x1);
        expansionController2 = i2cBus.getDevice(0x2);
        expansionController3 = i2cBus.getDevice(0x3);
        expansionController4 = i2cBus.getDevice(0x4);

        delay(1000);

        resetExpansionControllers();

        // Send an "Enable" Byte to DC and Servo controller chips and EXPANSIONansion controllers
        // enable command so that the robots won't move without a PrizmBegin statement
        motorControllerPrizmChip.write((byte) 0x25);
        delay(10);
       /* servoPrizmChip.write((byte) 0x25);
        delay(10);
        expansionController1.write((byte) 0x25);
        delay(10);
        expansionController2.write((byte) 0x25);
        delay(10);
        expansionController3.write((byte) 0x25);
        delay(10);
        expansionController4.write((byte) 0x25);
        delay(10);*/
    }

    public void prizmEnd() throws IOException {
        resetExpansionControllers();
    }

    // TODO readBatteryVoltage readLineSensor readSonicSensorCM readSonicSensorIN readStartButton setServoSpeed setServoSpeeds setServoPosition setServoPositions setCRServoState

    private void resetExpansionControllers() throws IOException {
        delay(4000);
        try{
            motorControllerPrizmChip.write((byte) 0x27);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        delay(10);
        /*servoPrizmChip.write((byte) 0x27);
        delay(10);
        expansionController1.write((byte) 0x27);
        delay(10);
        expansionController2.write((byte) 0x27);
        delay(10);
        expansionController3.write((byte) 0x27);
        delay(10);
        expansionController4.write((byte) 0x27);
        delay(10);*/
    }

    /**
     * @param motorChannel 1 or 2
     * @param power        -100 to 100
     * @throws Exception
     */
    public void setMotorPower(int motorChannel, int power) throws Exception {
        if (motorChannel == 1) {
            motorChannel = 0x40;
        } else if (motorChannel == 2) {
            motorChannel = 0x41;
        } else {
            throw new Exception("Invalid motor channel");
        }


        motorControllerPrizmChip.write((byte) motorChannel);
        motorControllerPrizmChip.write((byte) power);

        delay(10);
    }

    private void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
