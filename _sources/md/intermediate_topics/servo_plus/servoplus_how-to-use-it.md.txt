# How to use it

This is an extender of the servo class which allows you to set a position in degrees versus the 0,1 as is usual.

```java
ServoPlus(hardwareMap, "servo", 180);
```

You will need to put in 180 or 360 to set the position correctly.

Then just use,

```java
servo.setPosition(90); // sets it to 90 degrees
```

