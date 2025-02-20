# How to use it

[//]: # ([This demo opmode]&#40;../src/main/java/org/gentrifiedApps/gentrifiedAppsUtil/LoopTimeDemo.java&#41; is very handy in getting you started with the LoopTimeController. Initialize the controller in the init section of your code.)

```java
ElapsedTime elapsedTime = new ElapsedTime();
LoopTimeController loopTimeController = new LoopTimeController(elapsedTime,null,null);
```

Then, use the functions update and telemetry in order to see the current lps and loops in your telemetry. This is done in the main loop of your code.

```java
loopTimeController.update();
            
loopTimeController.telemetry(telemetry);
telemetry.update();
```

Thats it! Just monitor it from your DS telemetry.
