# Avoidance

## Overview

`AvoidanceController` manages a set of `VectorField` objects to provide drive power corrections for
obstacle avoidance
or field-based behaviors.

## Creating an AvoidanceController

```java
// Create VectorFields
VectorField field1 = new VectorField(0, 0, 10);
VectorField field2 = new VectorField(20, 10, 8);

// Initialize AvoidanceController with fields
AvoidanceController avoidanceController = new AvoidanceController(field1, field2);
```

## Adding and Removing Fields

```java
avoidanceController.addField(new VectorField(30, 15,5));
        avoidanceController.

removeField(field1);
```

## Using in Your OpMode

```java
// Get drive coefficients for the robot's current position
Point robotPosition = new Point(5, 7);
DrivePowerCoefficients coefs = avoidanceController.update(robotPosition);
// Use coefs to set wheel power
```

## Drawing Fields (Dashboard Visualization)

```java
avoidanceController.drawFields();
```

## Checking if a Point is in a Field

```java
boolean isInField = avoidanceController.inField(robotPosition) != null;
```

---

**Note:**

- `update(point)` returns `DrivePowerCoefficients` for the current field, or zeros if not in any
  field.
- Each `VectorField` can be configured for X, Y, BOTH, or OFF avoidance using the
  `avoidanceVectorType` parameter.

---

## Example: Minimal OpMode

```java
import org.firstinspires.ftc.teamcode.ggutil.AvoidanceController;
import org.firstinspires.ftc.teamcode.ggutil.VectorField;
import org.gentrifiedApps.gentrifiedAppsUtil.classes.drive.DrivePowerCoefficients;
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Point;


public class AvoidanceExample {
    public void run() {
        AvoidanceController avoidanceController = new AvoidanceController(new VectorField(0, 0, 10));
        Point robotPosition = new Point(0, 5);
        DrivePowerCoefficients coefs1 = // your coefs
        DrivePowerCoefficients coefs2 = avoidanceController.update(robotPosition);
        // Use coefs for driving logic
        // set power to coefs1.applyAvoidance(coefs2);
    }
}
```
