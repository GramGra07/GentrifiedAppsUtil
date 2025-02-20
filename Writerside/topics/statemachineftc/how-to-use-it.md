# How to use it

### Builder Function

There are a total of five builders you can choose from.

It is required to have both a state and transition builder for each state and the first state **must** have a onEnter command.

#### Inside the <> brackets, you **must** put the enum you created earlier.

```
StateMachine.Builder<name> builder = new StateMachine.Builder<>();
builder.state(name.STATE1)
        .onEnter(name.STATE1, () -> System.out.println("Entering STATE1"))
        .name(name.STATE1, () -> true)
        .state(name.STATE2)
        .onEnter(name.STATE2, () -> System.out.println("Entering STATE2"))
        .transition(name.STATE2, () -> true)
        .stopRunning(name.STOP);
StateMachine<name> stateMachine = builder.build();
```

The builder function builds the StateMachine. It is a function that returns a built StateMachine with all of the states and transitions you have added to it.

`.state()`

This method is used to add a state to the state machine. It must be created in order to have it all work correctly. You must add a state for each step in the program or it will throw errors when you try to run it.

`.onEnter(state, ()->{ function to run })`

This builder is used to add a function that will run when the state is entered. This is mainly used to start your state function, this meaning that it will start the runToPosition in this step if you are using basic encoder drive. Specifically with Road Runner, it can be used to start an async trajectory. It is also useful in order to perform additional robot setup before running your program.

`.whileState(state, ()-> condition (when true, will break loop) , ()-> function to run)`

This builder is used to add a function that will run while the supplied condition is active. When the condition becomes true, it will move on. This is extremely useful when you are using PID, or Road Runner specifically because they require calls to them constantly in a while loop in order to run correctly. For younger teams, this could be useful to make sure an arm is set to the correct spot.

`.onExit(state, ()->{ function to run })`

This builder is used to add a function that will run when the state is exited. You would use this to open a claw, or raise your arm for instance. It is useful to make sure something is set up correctly before the next step if you can't add it into the next onEnter.

`.transition(state, ()-> condition, int delay)`

This builder is used to add a condition that will be checked to see if the state should be switched. If the condition is true, it will move on. You must add this builder at the end of the state method. Essentially the amount of states, must equal the amount of transitions.

The delay is taken in seconds and will be run during the transition into the next state. This is useful to pause the robot for a second before moving on to the next step.

`.stopRunning(state)`

This builder is required to add a state that will stop the state machine from running.

`.build()`

This builder is required to build the state machine.

`() -> {}`

This is a lambda function that is used to pass a function into the builder that will be run when it is called. A lambda function is a function that the user will pass in, and then it will be used and called by the state machine while running.

`() ->`

This is a supplier for the transition or while state that will return a boolean value. You can make it be `()-> true` or `()-> false` or you can make it be `()-> opModeIsActive()`.
