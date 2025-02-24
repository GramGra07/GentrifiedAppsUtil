# TankDriver


This is a very easy way to implement mecanum code easily. To do it, just use

```java
driveTank(l, r);
// where l is the left joystick and r is the right joystick
```

It then returns DrivePowerCoefficients, which return values of frontLeft, frontRight, backLeft, backRight.;

```java
coefficients.frontLeft // this will return the double for front left
```

You can also use `driveTankRobotCentric` to drive tank without left and right joystick individual values.

```java
driveTankRobotCentric(drivePower, rotation);
// where drivePower is the power of the drive and rotation is the rotation of the robot
```

This will return DrivePowerCoefficients, which return values of frontLeft, frontRight, backLeft, backRight.;

```java
coefficients.frontLeft // this will return the double for front left
```