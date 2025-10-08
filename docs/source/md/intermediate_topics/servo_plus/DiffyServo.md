# DiffyServo

The `DiffyServo` class provides easy control over two servos, allowing for differential control. This class is part of the `org.gentrifiedApps.gentrifiedAppsUtil.classExtenders.servo` package.

## Constructor

There are multiple constructors available to initialize the `DiffyServo` class:

1. **Basic Initialization**:
    ```java
    DiffyServo diffyServo = new DiffyServo(hardwareMap, "leftServo", "rightServo");
    ```
    - `hardwareMap`: The hardware map of the robot.
    - `leftServo`: The name of the left servo.
    - `rightServo`: The name of the right servo.

2. **With Directions**:
    ```java
    DiffyServo diffyServo = new DiffyServo(hardwareMap, "leftServo", "rightServo", Servo.Direction.FORWARD, Servo.Direction.REVERSE);
    ```
    - `leftDirection`: The direction of the left servo.
    - `rightDirection`: The direction of the right servo.

3. **With Idle Position**:
    ```java
    DiffyServo diffyServo = new DiffyServo(hardwareMap, "leftServo", "rightServo", 0.5);
    ```
    - `idlePosition`: The idle position of the servos.

4. **Full Initialization**:
    ```java
    DiffyServo diffyServo = new DiffyServo(hardwareMap, "leftServo", "rightServo", Servo.Direction.FORWARD, Servo.Direction.REVERSE, 0.5);
    ```

## Methods

### setPosition
Sets the position of the servos to the current values of `lPos` and `rPos`.
```java
diffyServo.setPosition();
```

### rotateDown
Rotates both servos down by the specified position.
```java
diffyServo.rotateDown(0.1);
```

### rotateUp
Rotates both servos up by the specified position.
```java
diffyServo.rotateUp(0.1);
```

### rotateLeft
Rotates the servos to the left by the specified position (opposite directions).
```java
diffyServo.rotateLeft(0.1);
```

### rotateRight
Rotates the servos to the right by the specified position (opposite directions).
```java
diffyServo.rotateRight(0.1);
```

### applyPreset
Uses a `DiffyPreset` to set the left and right positions.
```java
DiffyPreset preset = new DiffyPreset(0.2, 0.8);
diffyServo.applyPreset(preset);
```

## Example Usage

```java
HardwareMap hardwareMap = // obtain hardware map
DiffyServo diffyServo = new DiffyServo(hardwareMap, "leftServo", "rightServo");

// Set initial position
diffyServo.setPosition();

// Rotate servos
diffyServo.rotateUp(0.1);
diffyServo.rotateLeft(0.1);

// Apply a preset
DiffyPreset preset = new DiffyPreset(0.2, 0.8);
diffyServo.applyPreset(preset);
```