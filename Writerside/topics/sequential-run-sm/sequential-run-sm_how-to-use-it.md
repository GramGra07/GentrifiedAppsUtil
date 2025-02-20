# How to use it

You can use the Sequential Run State Machine or SRSM to run things in a sequential order, waiting for a condition to be true in order to continue to the next state

```java
SequentialRunSM.Builder<States> builder = new SequentialRunSM.Builder<>();
        builder.state(States.STATE1).onEnter(States.STATE1, () -> {
            System.out.println("Entering STATE1");
        }).transition(States.STATE1, () -> {
            return true;
        }).state(States.STATE2).onEnter(States.STATE2, () -> {
            System.out.println("Entering STATE2");
        }).transition(States.STATE2, () -> {
            System.out.println("Transitioning from STATE2 to STATE3");
            return false;
        }).state(States.STATE3).onEnter(States.STATE3, () -> {
            System.out.println("Entering STATE3");
        }).transition(States.STATE3, () -> {
            System.out.println("Transitioning from STATE3 to STOP");
            return true;
        }).stopRunning(States.STOP);

        SequentialRunSM<States> srsmb = builder.build();
        srsmb.start();
        srsmb.update();
        srsmc.restartFromBeginning()
```

First you create the enum class with all of your states

Then, instantiate the builder class, as seen on the first line

Then, add your .state() and .onEnter() methods, these will be the basis of your entire SM

Then, add your .transition() conditions in order to continue through the states, when it returns true, it will pass through and go to the next condition

Then, use the .build command to start the machine

Finally use the .start() and .update() function in order to start and update the SRSM, the .update must be called every "loop" in your code

If you use .isRunning condition, it will return just that, if the SRSM is running or not

If you are using this in TeleOp and want it to be a "reusable" action, you need to use the last line of .restartFromBeginning().

```java
//Example usage in teleop
if (gamepad1.cross){ // button press
    if (!srsmb.isStarted){ // should only start if it wasnt already started
        srsmb.start(); // starts the SM
    }else{ // if already started
        srsmb.update(); //update the SM
    }
}else{// if not pressed
    srsmb.restartFromBeginning(); //restart the machine
}
```
