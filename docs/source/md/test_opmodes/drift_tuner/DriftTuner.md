# DriftTuner

Once you have found out that you do have drift, you may want to normalize all the values.

To add the drift constraints found in the Detection, use the ones after FrontLeft... it should have 4 numbers, input those into this next sequence:

```Java
driver.addDriftCorrection(new DrivePowerConstraint(1.0,1.0,1.0,1.0));
```

Then, all you need to do is:

```Java
driver.setWheelPower(Driver.applyDriftCorrection(powerCoefficients));
```

This will apply the drift correction to the power coefficients you are using in your driver.