# How to Use the Slow Mode Manager

## Introduction

The `SlowModeManager` is a utility designed to manage slow mode configurations for an FTC robot,
enabling dynamic speed
adjustments using gamepad inputs. This allows for more precise control, particularly during delicate
maneuvers.

**Note:** The slow mode factor is a DIVISION factor, meaning a factor of `2.0` will halve the speed.

### **Creating a SlowModeManager**

There are multiple ways to initialize a `SlowModeManager`:

```java
SlowModeManager slowModeManager = SlowModeManager(new SlowModeMulti(SlowMode.basic()));
```

#### Constructors

- `SlowModeManager(SlowModeMulti slowModeMulti)`

#### **Core Methods**

- `double apply(double value)` - Adjusts a given value based on the currently active slow mode.
- `DrivePowerCoefficients apply(DrivePowerCoefficients drivePowerCoefficients)` - Modifies drive
  power coefficients.
- `void update(onFunc,offFunc)` - Updates the active slow mode based on gamepad input. OnFunc being
  the function to call
  when the slow mode should be activated, and offFunc being the function to deactivate it.
- `void telemetry(Telemetry telemetry)` - Displays the current slow mode in telemetry.

---

### 2. `SlowModeMulti`

The `SlowModeMulti` class defines an individual slow mode configuration, specifying activation and
deactivation buttons.

#### **Creating a SlowModeMulti**

```java
SlowModeMulti slowMode = new SlowModeMulti(SlowMode.basic());
```

#### The Constructors

- `SlowModeMulti(SlowMode slowModeData)`

#### **Built-in Configurations**

- `static SlowModeMulti basic()` - Returns a slow mode with a factor of `2.0`.

---

### 3. `SlowMode`

The `SlowMode` class defines a slow mode with a configurable factor.

#### **Creating a SlowMode**

```java
SlowMode slowMode = new SlowMode(2); // Reduces speed by half
```

#### Singular Constructor

- `SlowMode(double slowModeFactor)`

#### **Predefined Modes**

- `static SlowMode basic()` - A factor of `2.0`.
- `static SlowMode one()` - A factor of `1.0` (no change).
- `static SlowMode of(double slowModeFactor)` - Creates a custom slow mode factor.

---

## Usage Example

This example demonstrates how to integrate `SlowModeManager` with a gamepad.

```java
SlowMode slowMode = new SlowModeMulti(SlowMode.basic()));
SlowModeManager slowModeManager = new SlowModeManager(slowMode);

// Applying slow mode to a value
double modifiedSpeed = slowModeManager.apply(1.0);
```

With this setup, your FTC robot can dynamically adjust its movement speed based on gamepad input,
providing better
control during gameplay.

