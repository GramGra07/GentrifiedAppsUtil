(config-creator)=
# Config Creator

Basically, the config creator allows you to make your config in your code, then build it from an opmode you can run. There are two ways you can do this, one is incredibly preferred over the other for simplicity.

**You MUST run the configuration scan first to see what expansion hub and IMU you have, it will not work without this.**

## Primary Method

**The preferred method is to use the Config Creator website.**

Use this site to generate your config: [Config Creator](https://gramgra07.github.io/ConfigurationCreator/)

Then download it and put it in your teamcode folder in your project.

Run the ConfigCreator and it will generate the config for you. Then simply activate that configuration and you are done!

## Secondary Method

The other method is to create the config in your code, then run the opmode to generate the config. This is not recommended as it is much more difficult and time consuming.

Here is an example of how to create a config in your code:

```java
ConfigMaker config = new ConfigMaker("pinkbot")
.addMotor("f1", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 0)
.addMotor("fr", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 2)
.addMotor("bl", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 1)
.addMotor("br", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 3);
```

Then, you can run the ConfigCreator opmode (below) to generate the config. This will create a config file in your teamcode folder.

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
            .addMotor("f1", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 0)
            .addMotor("fr", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 2)
            .addMotor("bl", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 1)
            .addMotor("br", ConfigMaker.ModuleType.CONTROL_HUB, ConfigMaker.MotorType.RevRoboticsUltraplanetaryHDHexMotor, 3);

    static boolean isEnabled = false;

    private ConfigRegistrar() {
    }

    private static OpModeMeta metaForClass(Class<? extends OpMode> cls) {
        return new OpModeMeta.Builder()
                .setName(cls.getSimpleName())
                .setGroup("Config")
                .setFlavor(OpModeMeta.Flavor.TELEOP)
                .build();
    }

    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        if (!isEnabled) return;
        manager.register(metaForClass(ConfigCreator.class), new ConfigCreator(config));
    }
}
    
```