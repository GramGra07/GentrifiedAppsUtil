# Timeout

The timeout is a great way to update and check if a timeout has been reached in your program async.

## Usage instructions

To start it, use either `Timeout timeout = new Timeout(time, () -> { //code })` or `Timeout timeout = new Timeout(time, () -> { //code }).start()`.

Then, use `timeout.update()` in your code to update the timeout this returns true if it has timed out or if your condition has passed.

You can also use `timeout.isTimedOut()` to check if the timeout has been reached.
