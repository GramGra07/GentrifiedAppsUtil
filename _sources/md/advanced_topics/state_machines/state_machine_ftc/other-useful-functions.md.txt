# Other useful functions

`machineName.mainLoop(this)`

This function contains the main loop of the StateMachine. It takes in your current opMode as a parameter and will run the while loop for you automatically. `while(machine.mainLoop(this)){`. It will allow you to have easier use of the State Machine because it will control it all for you without any other work.

`machineName.start()`

This function starts the StateMachine and must go immediately before the while loop.

`machineName.update()`

This function tells the StateMachine to update the current state and check if it should switch states and what it should currently be doing. It will be placed in the main while loop, which tells the State Machine to run the next step, or keep doing what it is doing.

`machineName.stop()`

This function completely stops the State Machine, no matter what step it is on or where it is in the process.
