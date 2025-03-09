package org.gentrifiedApps.gentrifiedAppsUtil.config;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@TeleOp
public class ConfigCreator extends LinearOpMode {
    public ConfigCreator(ConfigMaker configMaker){
        this.configMaker = configMaker;
    }
    ConfigMaker configMaker;

    @Override
    public void runOpMode() {
        configMaker.run();
        waitForStart();
    }
}

