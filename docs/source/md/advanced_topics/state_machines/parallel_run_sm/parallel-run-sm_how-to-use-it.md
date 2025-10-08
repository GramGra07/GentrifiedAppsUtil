# How to use it

You can use the Parallel Run SM to run as many states and actions at the same time as you want to, anywhere in your program.

```java
ParallelRunSM.Builder<States> builder = new ParallelRunSM.Builder<>();
        builder.state(States.STATE1)
                .onEnter(States.STATE1, () -> {
                })
                .state(States.STATE2)
                .onEnter(States.STATE2, () -> {
                })
                .state(States.STATE3)
                .onEnter(States.STATE3, () -> {
                })
                .stopRunning(States.STOP,()-> condition );
        ParallelRunSM<States> stateMachine = builder.build(use_timeout,timeout);
        stateMachine.start();
        stateMachine.update();
```

First you will create the enum with your states

Then, create your builder, notated on the first line, this will instantiate the State Machine and make it ready to start running

Then use the .state and .enter in order to  add the state to the action and corresponding state

Add a function into the .onEnter function and it will run it

Using the .start() method will start the SM and make the .isRunning() method to true

Then call .update() every time in your code and it will automatically run the SM and do all the actions provided

Finally, add a condition to the .stopRunning method that will make it exit when true. You can also add a timeout to the builder.build() method in order to make sure nothing breaks completely
