# Setup

The generic setup for this is incredibly easy, just create the `OpModeRepeaterRegistrar` class and copy in this code. 
Then, rename your drive motors in the `Driver` constructor to match your robot.
Then, change the `name` variable to the name of your recording if you want to.

Then you're ready to start repeating teleop modes!

```java
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.Driver;
import org.gentrifiedApps.gentrifiedAppsUtil.teleopTracker.TeleOpCopyRunner;
import org.gentrifiedApps.gentrifiedAppsUtil.teleopTracker.TeleOpTrackerOpMode;

public final class OpModeRepeaterRegistrar {

    static String name = "MyFirstRecording";
    static Driver driver = new Driver("fl", "fr", "bl", "br", DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE);

    static boolean isEnabled = true;

    private OpModeRepeaterRegistrar() {
    }

    private static OpModeMeta metaForClass(Class<? extends OpMode> cls, OpModeMeta.Flavor flavor) {
        return new OpModeMeta.Builder()
                .setName(cls.getSimpleName())
                .setGroup("Repeater")
                .setFlavor(flavor)
                .build();
    }

    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        if (!isEnabled) return;
        manager.register(metaForClass(TeleOpCopyRunner.class, OpModeMeta.Flavor.AUTONOMOUS), new TeleOpCopyRunner(name, driver));
        manager.register(metaForClass(TeleOpTrackerOpMode.class, OpModeMeta.Flavor.TELEOP), new TeleOpTrackerOpMode(name, driver));
    }
}
```