package org.gentrifiedApps.gentrifiedAppsUtil.config;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;


public final class ConfigRegistrar {

    static ConfigMaker config = new ConfigMaker("if you see this tell me")
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
    