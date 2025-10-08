# 1DfAutoLevel

1 Df Auto Level is a class that allows you to automatically level your claw given a certain rotation
of a lift. 1 Df
corresponds to 1 degree of freedom, which is the rotation of the lift.

Where lift is the rotation of the lift, and there are **two** joints of rotation, the claw and the
lift.

## How to use it

To use it, you need to create the instance of the class with the following parameters:

``` java
AutoLevel1DF(slope, offsetInitial, slopeIntercept, servo);
// where slope is the slope of the line from input to output, offsetInitial is the initial offset of the servo at level
// and optional params slopeIntercept to have more control of the y = mx + b equation, and servo is the servo to use at the output, or you can just use raw functions to get the data.
```

```java
AutoLevel1DF autoLevel = new AutoLevel1DF(0.5, -30.0, new SlopeIntercept(0.5, 20), servo);

findAlphaVal(30); // where 30 is the angle of the lift in degrees AKA theta input - this returns the calculated servo position

setServoPosition(theta, servo); // where theta is the angle of the lift in degrees, servo is the servo to set

setServoPositionWithOffset(theta, servo, offset); // where offset is the offset to apply to the servo position AKA not 100% level
```

## The tuner

If you have used anything else in this library, you may have a register class already, if not, make
one following the
example in {ref}`config-creator`.

Then, add the following line to your register class:

```java
manager.register(metaForClass(AutoLevel1DfTuner.class),new

AutoLevel1DfTuner(
        outputServoName,
        inputEncoderName,
        potentiometerName,
        convertFactor:Double,
        ));
// where convertFactor is the factor to convert the encoder ticks to degrees, if you don't know it, just run the tuner with 1.0 as the input and it will tell you what it is.
```

This will register the OpMode with the name "AutoLevel1DfTuner." Run it, and it will tell you what
to do!

## Math Explanation

The math behind this is actually very simple, I will refer to the input as 'theta' and output as '
alpha' for
convenience.

The main goal is to find the angle of the servo given the angle of the lift where theta is the
bottom of the triangle,
and alpha is the top of the triangle.

There is a linear relationship between theta and alpha, as in SlopeIntercept form, which for me, I
found to be -.26x+96,
however, I was using a gear reduction, so the slope will be different, but I would expect it to be
negative.

The equation is:

```
alpha = ((slope * (theta - offsetInitial)) + slopeIntercept) + finalOffset
```

The fun part of this is that the input shaft is more than usually 1800 ticks, which means you need
to calculate that
yourself by converting it to degrees. The easiest way is to get the ticks of the encoder, and find
`degree/ticks` which
when multiplied by the ticks will give you the degrees.

The most effective way is just to run a test to get the encoder ticks, then move it to 90 degrees
and get the ticks.
Then do `90/ticks` to get the effective conversion factor.