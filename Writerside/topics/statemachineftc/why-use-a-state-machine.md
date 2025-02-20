# Why use a State Machine?

The architecture of the state machine allows for an easier and simplified way to move the robot through autonomous using the conditions of the robot to control it.

Using a state machine allows for a more efficient way of programming autonomous.

FTC Sample code programs the autonomous very linearly and one by one. As soon as you need other motor control or PIDF or a reason to use the while(opModeIsActive()) loop, it becomes much more difficult to program because it is linear. The StateMachine helps solve this problem and makes it easier to program.

### How does it work?

Once you code the builder function, you call it in your autonomous program and it will run through each state systematically with each function and condition you pass into it.

It will run through step by step, waiting for a condition to be true to move on to the next state.
