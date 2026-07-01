package org.gentrifiedApps.gentrifiedAppsUtil.config;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;


public final class ConfigRegistrar {

    static ConfigMaker config = new ConfigMaker("if you see this tell me")
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
