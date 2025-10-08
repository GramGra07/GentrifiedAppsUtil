# What is a State Machine?

More information on State Machines can be found [here](https://state-factory.gitbook.io/state-factory/essential-usage).

A state machine is a way of linearly programming an autonomous architecture. This meaning that it will go step by step through the code and run each function in order. Autonomous architecture refers to the way you structure and build your autonomous.

It allows it to switch states and way things are moving during autonomous based on the conditions of the robot. This is useful because the robot will only do the "next step" when it is ready to do so based on a condition you provide to it.

Specifically for FTC, things like PIDF control and other motor controls require the loop to constantly be running. FTC sample code almost never uses the while(opModeIsActive()) loop, but instead just programs it linearly. For younger teams as well, this is a way to make it easier to understand and program the autonomous architecture.

FTC's basic autonomous using encoder drive uses just linear code and while loops waiting for the motors to complete running to their positions before starting the next step. The State Machine allows us to do this directly in the code, without using while loops much or at all.
