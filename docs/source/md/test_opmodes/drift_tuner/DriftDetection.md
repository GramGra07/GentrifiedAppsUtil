# DriftDetection

If you feel like your motors are not all running at the correct speed, you can test for this!

Simply make an OpModeRegister class, like in ConfigCreator, and add the following line (and make sure encoders are plugged in):

```Java
manager.register(metaForClass(DriftTunerOpMode.class, OpModeMeta.Flavor.AUTONOMOUS), new DriftTunerOpMode(new Driver("fl", "fr", "bl", "br", DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE),3));
```

Make sure that the driver is the same as the one you are using in your robot. The three in this example is the number of seconds it will run at full power for.

You may need to change the metaForClass to not include AUTONOMOUS if you are using a different OpMode or register type.

The tuner will show the percent of max speed the wheels can actually run at.