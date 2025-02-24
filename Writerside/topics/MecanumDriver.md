# MecanumDriver


This is a very easy way to implement mecanum code easily. To do it, just use

```java
driveMecanum(x,y,rotation);
// where x is the x on the controller
// where y is the y on the controller
// where rotation is the rotation from the controller
```

It then returns DrivePowerCoefficients, which return values of frontLeft, frontRight, backLeft, backRight.;

```java
coefficients.frontLeft // this will return the double for front left
```

