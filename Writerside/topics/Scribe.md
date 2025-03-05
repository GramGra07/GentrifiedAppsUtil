# Scribe

Scribe is a logger for FTC that allows you to view certain actions that have happened in your code through the logcat.

To access this, use Rev Hardware Client and view the logs.

## How to use it

There is no initialization steps needed, simply use `Scribe.getInstance()` to get the instance of the logger.

Then, you can use the following methods:

```java
Scribe.getInstance().logData("This is a log message");
Scribe.getInstance().logError("This is an error message");
Scribe.getInstance().logWarning("This is a warning message");
Scribe.getInstance().logDebug("This is a debug message");
Scribe.getInstance().startLogger("OpMode Name"); // this should be used at the very start of an OpMode that shows you when it gets started

// or

Scribe.create("tag"); // creates a new logger with the tag of your choosing
Scribe.reset(); // resets the logger back to default tag (Scribe)
```
