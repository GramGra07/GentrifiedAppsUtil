# Data Analysis

While using the LoopTimeController, you may be confused what all these values mean, this is a guide of what they mean and how to use them.

* Timer - Shows time in seconds
* Loops - Shows number of loops since start
* LPS - Shows the average loops per second, loops/time
  * If this is high, you're good, the lower you get, the more lag you experience
* Time Elapsed - Shows "ping" in ms of robot
  * If this is super high, you might have a problem with how long functions are taking
* Avg time elapsed - Shows **average** "ping" in ms of the robot
* Last second LPS - Shows the last LPS of the last second
  * More accurate than LPS as it is more recent and not an average

### So how do I fix low LPS?

Easiest way is to optimize everything thats running on the robot at a given time. If there is something running that doesn't need to be run, delete it. Otherwise use loopTimeController.every() in order to delay the running of some functions, prioritizing LPS. What this does is it only runs the function in every only on the "period" you set. For instance, if I tell my robot to build telemetry every 10 loops, it will run my telemetry function every 10 loops, therefore decreasing output to the DS, but gaining a couple loops back.

