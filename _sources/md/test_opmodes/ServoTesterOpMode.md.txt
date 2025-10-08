# ServoTesterOpMode

Throughout my years of FTC, the one thing I have always needed is a way to set a servo to the
precise position without
having to have a full running opMode to support it. This is what this OpMode is for.

## How to use it

If you have used anything else in this library, you may have a register class already, if not, make
one following the example in {ref}`config-creator`.

Then, add the following line to your register class:

```java
manager.register(metaForClass(ServoTesterOpMode.class),new

ServoTesterOpMode("servo"));
// if you want to you can also use a list of positions as doubles between -180 and 180 as the second parameter
```

This will register the OpMode with the name "ServoTesterOpMode." Run it, and it will tell you what
to do!