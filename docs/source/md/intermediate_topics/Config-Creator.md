(config-creator)=

# Config Creator

Basically, the config creator allows you to make your config in your code, then build it from an
opmode you can run.

## Primary Method

The primary method is to create the config in your code, then run the opmode to generate the config. There is the ability now to use the detection methods, which after creating a config file, will streamline the experience. The first step is to create a new configuration called "scan", you will only hit the scan button and then save the configuration. Then follow the rest of this guide.

Here is an example of how to create a config in your code:

```java
ConfigMaker config = new ConfigMaker("pinkbot")
.addMotor("f1", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 0)
.addMotor("fr", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 2)
.addMotor("bl", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 1)
.addMotor("br", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 3);
// use addModule_detect() to add an expansion hub easily
// use addIMU_detect(MODULE TYPE) to add an IMU on the c-hub or e-hub
// use addCamera_detectSN() to add a camera easily using the scan config

```

Then, you can run the ConfigCreator opmode (below) to generate the config. This will create a config
file in your teamcode folder.

## List of Types

**Modules:**

CONTROL_HUB,
EXPANSION_HUB

**Devices:**

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

**Motors:**

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

## Lastly, you can also just download or create this file

You will need to import things.

```java
public final class ConfigRegistrar {

    static ConfigMaker config = new ConfigMaker("pinkbot")
    .addModule_detect() // will automatically detect using the scan config
    .addIMU_detect(ConfigMaker.ModuleType.CONTROL_HUB)// ^ same
            .addMotor("f1", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 0)
            .addMotor("fr", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 2)
            .addMotor("bl", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 1)
            .addMotor("br", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 3);

    static boolean isEnabled = false;

    private ConfigRegistrar() {
    }

    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        if (!isEnabled) return;
        manager.register(config.metaData(), new ConfigCreator(config));
    }
}
    
```
