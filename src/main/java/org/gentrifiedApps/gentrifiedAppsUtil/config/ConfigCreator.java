package org.gentrifiedApps.gentrifiedAppsUtil.config;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.gentrifiedApps.gentrifiedAppsUtil.classes.Scribe;

//@TeleOp
public class ConfigCreator extends LinearOpMode {
    ConfigMaker configMaker;

    public ConfigCreator(ConfigMaker configMaker) {
        this.configMaker = configMaker;
    }

    @Override
    public void runOpMode() {
        configMaker.run();
        Scribe.getInstance().logDebug("Config Maker run");
        Scribe.getInstance().logDebug("XML: " + configMaker.getXML());
        waitForStart();
    }
}

