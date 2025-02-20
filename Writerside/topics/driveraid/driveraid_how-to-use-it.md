# How to use it

The DriverAid class allows you to control the way your driver aid gets run. You can add DriverAid functions to the class, then set the state in order to run it constantly.

It takes one parameter of an enum to set the state, then, you add a function DAFunc that will run the functions. funcs run when it is initialized, and everything is run on DriverAid.update().

You must do DriverAid.daState = state, then DriverAid.runInit()

More info on the documentation through the library.
