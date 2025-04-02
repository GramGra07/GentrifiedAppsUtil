package org.gentrifiedApps.gentrifiedAppsUtil.config;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is used to create a configuration file for the robot controller
 */
public class ConfigMaker {
    private static String name;
    /**
     * Adds a module to the configuration, **DO NOT add a Control Hub**
     *
     * @param type
     * @param name
     */
    boolean added = false;
    private boolean hasBeenBuilt = false;
    private String XML = "";
    private String CHUB = "     <LynxModule name=\"Control Hub\" port=\"173\">\n";
    private String EHub = "";
    private String WC = "";

    /**
     * Creates a new ConfigMaker
     *
     * @param configName The name of the configuration file
     */
    public ConfigMaker(String configName) {
        name = configName;
    }

    String getXML() {
        return XML;
    }

    /**
     * Adds a device to the configuration
     *
     * @param name       The name of the device
     * @param moduleType The type of module the device is connected to
     * @param type       The type of device
     * @param port       The port the device is connected to
     */
    public ConfigMaker addDevice(String name, ModuleType moduleType, DeviceType type, int port) {
        if (port > 10) {
            throw new IllegalArgumentException("Device port must be less than 10");
        } else if (port < 0) {
            throw new IllegalArgumentException("Device port must be greater than 0");
        }
        String typeName;
        if (type == DeviceType.REV_INTERNAL_BNO055_IMU){
            typeName = "LynxEmbeddedIMU";
        }else if (type == DeviceType.REV_INTERNAL_BHI260_IMU) {
            typeName = "ControlHubImuBHI260AP";
        }else{
            typeName = type.toString();
        }
        String s = String.format("         <%s name=\"%s\" port=\"%s\"/>\n", typeName, name, port);
        if (type == DeviceType.RevColorSensorV3 || type == DeviceType.RevExternalImu || type == DeviceType.AdafruitBNO055IMU || type == DeviceType.HuskyLens || type == DeviceType.SparkFunOTOS) {
            s += "bus=\"undefined\"";
        }
        if (moduleType == ModuleType.CONTROL_HUB) {
            CHUB += s;
        } else if (moduleType == ModuleType.EXPANSION_HUB) {
            EHub += s;
        }
        return this;
    }

    public ConfigMaker addModule(ModuleType type, String name) {

        if (type == ModuleType.EXPANSION_HUB && !added) {
            EHub = String.format("      <LynxModule name=\"%s\" port=\"1\">\n", name);
            added = true;
        }
        return this;
    }

    /**
     * Adds a camera to the configuration
     *
     * @param name         The name of the camera
     * @param serialNumber The serial number of the camera
     */
    public ConfigMaker addCamera(String name, String serialNumber) {
        WC += String.format("<Webcam name=\"%s\" serialNumber=\"%s\" />\n", name, serialNumber);
        return this;
    }

    public ConfigMaker addMotor(String name, ModuleType moduleType, MotorType type, int port) {
        if (port > 3) {
            throw new IllegalArgumentException("Motor port must be less than 3");
        } else if (port < 0) {
            throw new IllegalArgumentException("Motor port must be greater than 0");
        }
        String typeName;
        if (type == MotorType.NeveRest3_7v1Gearmotor) {
            typeName = "NeveRest3.7v1Gearmotor";
        } else {
            typeName = type.toString();
        }
        if (moduleType == ModuleType.CONTROL_HUB) {
            CHUB += String.format("         <%s name=\"%s\" port=\"%s\"/>\n", typeName, name, port);
        } else if (moduleType == ModuleType.EXPANSION_HUB) {
            EHub += String.format("        <%s name=\"%s\" port=\"%s\"/>\n", typeName, name, port);
        }
        return this;
    }

    /**
     * Builds the configuration file
     */
    public ConfigMaker build() {
        hasBeenBuilt = true;
        if (!EHub.equals("")) {
            EHub += "       </LynxModule>\n";
        }

        XML = "<Robot type=\"FirstInspires-FTC\">\n" +
                "    <LynxUsbDevice name=\"Control Hub Portal\" serialNumber=\"(embedded)\" parentModuleAddress=\"173\">\n" +
                CHUB +
                "       </LynxModule>\n" +
                EHub +
                "    </LynxUsbDevice>\n" +
                WC +
                "</Robot>";
//        System.out.println(XML);
        return this;
    }

    /**
     * Writes the configuration file
     */
    public void run() {
        if (!hasBeenBuilt) {
            build();
        }
        String filePath = String.format("%s/FIRST/%s.xml", Environment.getExternalStorageDirectory().getAbsolutePath(), name);
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(XML);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum ModuleType {
        CONTROL_HUB,
        EXPANSION_HUB
    }

    public enum DeviceType {
        AnalogInput,
        ContinuousRotationServo,
        Servo,
        RevSPARKMini,
        RevBlinkinLedDriver,
        AdafruitBNO055IMU,
        HuskyLens,
        OctoQuadFTC,
        REV_VL53L0X_RANGE_SENSOR,
        RevExternalImu,
        RevColorSensorV3,
        QWIIC_LED_STICK,
        SparkFunOTOS,
        RevTouchSensor,
        Led,
        DigitalDevice,
        OpticalDistanceSensor,
        ModernRoboticsAnalogTouchSensor,
        REV_INTERNAL_BNO055_IMU,
        REV_INTERNAL_BHI260_IMU,
    }

    public enum MotorType {
        goBILDA5201SeriesMotor,
        goBILDA5202SeriesMotor,
        Matrix12vMotor,
        NeveRest3_7v1Gearmotor,
        NeveRest20Gearmotor,
        NeveRest40Gearmotor,
        NeveRest60Gearmotor,
        RevRobotics20HDHexMotor,
        RevRobotics40HDHexMotor,
        RevRoboticsCoreHexMotor,
        RevRoboticsUltraplanetaryHDHexMotor,
        TetrixMotor,
        Motor
    }
}
