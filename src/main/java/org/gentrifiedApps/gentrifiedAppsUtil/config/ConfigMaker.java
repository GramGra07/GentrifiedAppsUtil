package org.gentrifiedApps.gentrifiedAppsUtil.config;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigMaker{
    private boolean hasBeenBuilt = false;
    private String XML = "";
    private String CHUB = "     <LynxModule name=\"Control Hub\" port=\"173\">\n";
    private String EHub = "";
    private String WC = "";
    public static enum ModuleType{
        CONTROL_HUB,
        EXPANSION_HUB
    }
    public static enum DeviceType{
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
    }
    public enum MotorType{
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
    private static String name;
    public ConfigMaker(String configName){
        this.name = configName;
    }
    public ConfigMaker addDevice(String name, ModuleType moduleType, DeviceType type, int port){
        String s = String.format("         <%s name=\"%s\" port=\"%s\"/>\n", type.toString(), name, port);
        if (moduleType == ModuleType.CONTROL_HUB) {
            CHUB += s;
        }else if (moduleType == ModuleType.EXPANSION_HUB){
            EHub += s;
        }
        return this;
    }

    /**
     * Adds a module to the configuration, **DO NOT add a Control Hub**
     * @param type
     * @param name
     */
    public ConfigMaker addModule(ModuleType type, String name){
        if (type == ModuleType.EXPANSION_HUB){
            EHub = String.format("      <LynxModule name=\"%s\" port=\"1\">\n",name);
        }
        return this;
    }
    public ConfigMaker addCamera(String name, String serialNumber){
            WC +=String.format("<Webcam name=\"%s\" serialNumber=\"%s\" />\n", name, serialNumber);
        return this;
    }
    public ConfigMaker addMotor(String name,ModuleType moduleType, MotorType type, int port){
        if (port > 3){
            throw new IllegalArgumentException("Motor port must be less than 3");
        }else if (port < 0){
            throw new IllegalArgumentException("Motor port must be greater than 0");
        }
        String typeName;
        if (type == MotorType.NeveRest3_7v1Gearmotor){
            typeName = "NeveRest3.7v1Gearmotor";
        }else{
            typeName = type.toString();
        }
        if (moduleType == ModuleType.CONTROL_HUB) {
            CHUB += String.format("         <%s name=\"%s\" port=\"%s\"/>\n", typeName, name, port);
        }else if (moduleType == ModuleType.EXPANSION_HUB){
            EHub += String.format("        <%s name=\"%s\" port=\"%s\"/>\n", typeName, name, port);
        }
        return this;
    }
    public ConfigMaker build(){
        hasBeenBuilt = true;
        if (!EHub.equals("")){
            EHub+= "       </LynxModule>\n";
        }

        XML = "" +
                "<Robot type=\"FirstInspires-FTC\">\n" +
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

    public void run(){
        if (!hasBeenBuilt){
            build();
        }
        String filePath = String.format("%s/FIRST/%s.xml", Environment.getExternalStorageDirectory().getAbsolutePath(),name);
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
}
