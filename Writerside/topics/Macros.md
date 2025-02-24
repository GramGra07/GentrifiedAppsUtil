# Macros

This is a system built that allows you to detect a sequence of button presses and run a function based on that. This is useful for things like changing the state of the robot or running a function based on a button press.

## How to use

```Java
Macro macro = new MacroBuilder().buttonPress(Button.CIRCLE).build();// your button presses

Runnable func = () -> // Function to run;

GamepadMacro gamepadMacro = new GamepadMacro(macro, func);

gamepadMacro.update(gamepadPlus.getButtonsCurrentlyPressed());
```

This will run the function when the button is pressed. You can also use the `GamepadMacro` class to run the function when the button is pressed.