# Other useful functions

## .every function

This allows you to run a function every so many loops.

```java
loopTimeController.every(3, () -> {
   telemetry.addData("Every 3", "Seconds");
});
```

This runs the function every 3 loops using the ()->{} to define the function being run.
