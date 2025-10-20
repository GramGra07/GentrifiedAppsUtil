# How to use it

[//]: # ([This demo opmode]&#40;../src/main/java/org/gentrifiedApps/gentrifiedAppsUtil/LoopTimeDemo.java&#41; is very handy in getting you started with the LoopTimeController. Initialize the controller in the init section of your code.)

```java
LoopTimeController loopTimeController = new LoopTimeController();
```

Then, use the functions update and telemetry in order to see the current lps and loops in your telemetry. This is done in the main loop of your code.

```java
loopTimeController.update();
            
loopTimeController.telemetry(telemetry);
telemetry.update();
```

Thats it! Just monitor it from your DS telemetry.

## Other useful functions

```java
loopTimeController.setLoopSavingCache(hardwareMap); // sets the bulk caching mode, saving around 30-40 LPS

loopTimeController.every(period,()->{//code goes here
});
// this allows you to control several things that don't need to be run 100% of the time, it only updates every "period" loops.
```

