# FieldCentricDriver

This is a very easy way to implement field centric drive to your code easily. To do it, just use

```java
driveFieldCentric(x,y,rotation,gyro);
// where x is the x on the controller
// where y is the y on the controller
// where rotation is the rotation from the controller
// where gyro is the current rotation of the robot
```

It then returns DrivePowerCoefficients, which return values of frontLeft, frontRight, backLeft, backRight.&#x20;

```java
coefficients.frontLeft // this will return the double for front left
```

