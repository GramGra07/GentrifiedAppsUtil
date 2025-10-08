# InitMovementController

As introduced in the 2024-25 season, you are not allowed to move at all on initialization. This
allows for easy coding
of this safeguard.

## How to use it

To use it, you need to create the instance of the class with the following parameters:

```java
InitMovementController name = new InitMovementController(gamepad1, gamepad2);
// then use these methods

// in your main loop after while (opModeIsActive()) 

name.checkHasMovedOnInit();

// then use 

if(name.hasMovedOnInit()){

        // do something
        
        // allow motors and servos to move

        }
```