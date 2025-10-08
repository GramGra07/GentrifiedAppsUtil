# DataStorage

DataStorage is a way you can store your position and alliance color from auto to teleOp. Most commonly this is used so teams can use the robots position from auto to teleOp and use teleOp automation.

## How to use it

To use it, you do not need to initialize the class at all. Simply just use the following code:

```java
DataStorage.getPose();
DataStorage.getAllianceColor();
```

Where `getPose()` returns the pose of the robot in `Target2D` object and Alliance returns `Alliance`.

To set the object, use the following code:

```java
DataStorage.setPose(pose);
DataStorage.setAlliance(alliance);
```

Where `pose` is the pose of the robot in `Target2D` object and `alliance` is the alliance color in `Alliance` object.

To use this, you can constantly do something like `DataStorage.setPose(currentPose)` during autonomous, and during init of teleOp, set your start position to `DataStorage.getPose()`.