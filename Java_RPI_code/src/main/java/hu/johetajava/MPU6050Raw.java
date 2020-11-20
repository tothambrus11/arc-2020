package hu.johetajava;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class MPU6050Raw {

    //Global Variables
    private static double GRAVITIY_MS2 = 9.80665;
    private I2CBus i2cBus;
    private I2CDevice device;

    //Scale Modifiers
    private static final double ACCEL_SCALE_MODIFIER_2G = 16384.0;
    private static final double ACCEL_SCALE_MODIFIER_4G = 8192.0;
    private static final double ACCEL_SCALE_MODIFIER_8G = 4096.0;
    private static final double ACCEL_SCALE_MODIFIER_16G = 2048.0;

    private static final double GYRO_SCALE_MODIFIER_250DEG = 131.0;
    private static final double GYRO_SCALE_MODIFIER_500DEG = 65.5;
    private static final double GYRO_SCALE_MODIFIER_1000DEG = 32.8;
    private static final double GYRO_SCALE_MODIFIER_2000DEG = 16.4;

    // Pre-defined Ranges
    public static final byte ACCEL_RANGE_2G = 0x00;
    public static final byte ACCEL_RANGE_4G = 0x08;
    public static final byte ACCEL_RANGE_8G = 0x10;
    public static final byte ACCEL_RANGE_16G = 0x18;

    public static final byte GYRO_RANGE_250DEG = 0x00;
    public static final byte GYRO_RANGE_500DEG = 0x08;
    public static final byte GYRO_RANGE_1000DEG = 0x10;
    public static final byte GYRO_RANGE_2000DEG = 0x18;

    // MPU-6050 Registers
    private static byte PWR_MGMT_1 = 0x6B;
    private static byte PWR_MGMT_2 = 0x6C;

    private static byte ACCEL_XOUT0 = 0x3B;
    private static byte ACCEL_YOUT0 = 0x3D;
    private static byte ACCEL_ZOUT0 = 0x3F;

    private static byte TEMP_OUT0 = 0x41;

    private static byte GYRO_XOUT0 = 0x43;
    private static byte GYRO_YOUT0 = 0x45;
    private static byte GYRO_ZOUT0 = 0x47;

    private static byte ACCEL_CONFIG = 0x1C;
    private static byte GYRO_CONFIG = 0x1B;

    // OFFSETS:
    private static final double GYRO_OFFSET_X = 0.05237804878048737;
    private static final double GYRO_OFFSET_Y = -1.4425609756097844;
    private static final double GYRO_OFFSET_Z = 1.0094817073170697;
    /**
     * This is a constructor that initializes a new MPU6050 with the passed in address. This assumes that you are using a RasPi2 or newer.
     *
     * @param address The I2C address of the MPU6050.
     */
    public MPU6050Raw(byte address) throws Exception {
        this.init(address, 1);
    }

    /**
     * This is a constructor that initializes a new MPU6050 with the passed in address.
     *
     * @param address i2c address of the MPU6050. Usually 0x68 or 0x69.
     * @param busNo   Bus number that will be used. 0 for RasPi1 1 for all other RasPi
     */
    public MPU6050Raw(byte address, int busNo) throws Exception {
        this.init(address, busNo);
    }

    private void init(byte address, int busNo) throws Exception {
        this.i2cBus = I2CFactory.getInstance(busNo);
        this.device = i2cBus.getDevice(address);
        // Wake up the MPU6050 from sleep since it starts in sleep mode by default.
        device.write(PWR_MGMT_1, (byte) 0x00);
    }

    //General i2c Communication Methods

    /**
     * Reads a word (16 bits) from the passed register and the register after. The data is combine.
     *
     * @param register Place that is being read from.
     * @return The value of the word read from the register.
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public int read_i2c_word(int register) throws Exception {
        int high = device.read(register);
        int low = device.read(register + 1);

        int value = (high << 8) + low;

        if (value >= 0x8000) {
            return -((65535 - value) + 1);
        } else {
            return value;
        }
    }

    //MPU6050 Methods

    /**
     * Reads the temperature from the MPU6050's onboard sensor.
     *
     * @return The temperature in degrees Celcius.
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public double get_temp() throws Exception {
        int rawTemp = read_i2c_word(TEMP_OUT0);
        return (rawTemp / 340d) + 36.53;
    }

    /**
     * Sets the range of the accelerometer.
     *
     * @param accelRange The range that the accelerometer is set to. Using one of the predefined ranges is advised.
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public void set_accel_range(byte accelRange) throws Exception {
        device.write(ACCEL_CONFIG, (byte) 0x00);
        device.write(ACCEL_CONFIG, accelRange);
    }

    /**
     * Reads the raw accelerometer range.
     *
     * @return The raw value from the ACCEL_COMFIG register.
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public double read_raw_accel_range() throws Exception {
        return device.read(ACCEL_CONFIG);
    }

    /**
     * Reads the accelerometer range in terms of gravity.
     *
     * @return Returns an integer: -1, 2, 4, 8, 16. If -1 an error occurred.
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public int read_accel_range() throws Exception {
        switch ((int) read_raw_accel_range()) {
            case ACCEL_RANGE_2G:
                return 2;
            case ACCEL_RANGE_4G:
                return 4;
            case ACCEL_RANGE_8G:
                return 8;
            case ACCEL_RANGE_16G:
                return 16;
            default:
                return -1;
        }
    }

    /**
     * Gets the x, y, and z accelerometer data.
     *
     * @return Returns x, y, z in a double array [x, y, z]. All values are in m/s^2.
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public double[] get_accel_data() throws Exception {
        return get_accel_data(false);
    }

    /**
     * Gets the x, y, and z accelerometer data.
     *
     * @param g If true: returned values are in terms of gravity(g). If false: returned values are in terms of m/s^2.
     * @return Returns x, y, z in a double array [x, y, z].
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public double[] get_accel_data(boolean g) throws Exception {
        double x = read_i2c_word(ACCEL_XOUT0);
        double y = read_i2c_word(ACCEL_YOUT0);
        double z = read_i2c_word(ACCEL_ZOUT0);

        double accel_scale_modifier;
        double accel_range = read_raw_accel_range();

        if (accel_range == ACCEL_RANGE_2G) {
            accel_scale_modifier = ACCEL_SCALE_MODIFIER_2G;
        } else if (accel_range == ACCEL_RANGE_4G) {
            accel_scale_modifier = ACCEL_SCALE_MODIFIER_4G;
        } else if (accel_range == ACCEL_RANGE_8G) {
            accel_scale_modifier = ACCEL_SCALE_MODIFIER_8G;
        } else if (accel_range == ACCEL_RANGE_16G) {
            accel_scale_modifier = ACCEL_SCALE_MODIFIER_16G;
        } else {
            System.out.println("Unkown range - accel_scale_modifier set to self.ACCEL_SCALE_MODIFIER_2G\"");
            accel_scale_modifier = ACCEL_SCALE_MODIFIER_2G;
        }

        x = x / accel_scale_modifier;
        y = y / accel_scale_modifier;
        z = z / accel_scale_modifier;

        if (!g) {
            x = x * GRAVITIY_MS2;
            y = y * GRAVITIY_MS2;
            z = z * GRAVITIY_MS2;
        }

        return new double[]{x, y, z};
    }

    /**
     * Sets the range of the Gyroscope.
     *
     * @param gyroRange The range to set the gyroscope to. Using a predefined range is advised.
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public void set_gyro_range(byte gyroRange) throws Exception {
        device.write(GYRO_CONFIG, (byte) 0x00);
        device.write(GYRO_CONFIG, gyroRange);
    }

    /**
     * Reads the raw range of the gyroscope.
     *
     * @return Returns the raw value from the GYRO_CONFIG register.
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public double read_raw_gyro_range() throws Exception {
        return device.read(GYRO_CONFIG);
    }

    /**
     * Reads the range of the gyroscope.
     *
     * @return Returns an integer: -1, 250, 500, 1000, and 2000. If -1 is returned something bad has happened.
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public double read_gyro_range() throws Exception {
        double rawData = read_raw_gyro_range();

        if (rawData == GYRO_RANGE_250DEG) return 250;
        else if (rawData == GYRO_RANGE_500DEG) return 500;
        else if (rawData == GYRO_RANGE_1000DEG) return 1000;
        else if (rawData == GYRO_RANGE_2000DEG) return 2000;
        else return -1;
    }

    /**
     * Gets the data from the gyroscope.
     *
     * @return Returns the values as a double array: [x, y, z].
     * @throws Exception FIXME passes all errors onto the surrounding program.
     */
    public double[] get_gyro_data() throws Exception {
        double x = read_i2c_word(GYRO_XOUT0);
        double y = read_i2c_word(GYRO_YOUT0);
        double z = read_i2c_word(GYRO_ZOUT0);

        double gyro_scale_modifier;
        double gyro_range = read_raw_gyro_range();

        if (gyro_range == GYRO_RANGE_250DEG) {
            gyro_scale_modifier = GYRO_SCALE_MODIFIER_250DEG;
        } else if (gyro_range == GYRO_RANGE_500DEG) {
            gyro_scale_modifier = GYRO_SCALE_MODIFIER_500DEG;
        } else if (gyro_range == GYRO_RANGE_1000DEG) {
            gyro_scale_modifier = GYRO_SCALE_MODIFIER_1000DEG;
        } else if (gyro_range == GYRO_RANGE_2000DEG) {
            gyro_scale_modifier = GYRO_SCALE_MODIFIER_2000DEG;
        } else {
            System.out.println("Unkown range - gyro_scale_modifier set to self.GYRO_SCALE_MODIFIER_250DEG");
            gyro_scale_modifier = GYRO_SCALE_MODIFIER_250DEG;
        }

        x = x / gyro_scale_modifier + GYRO_OFFSET_X;
        y = y / gyro_scale_modifier + GYRO_OFFSET_Y;
        z = z / gyro_scale_modifier + GYRO_OFFSET_Z;

        return new double[]{x, y, z};
    }

}
