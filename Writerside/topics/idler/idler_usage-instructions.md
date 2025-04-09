# Usage instructions

The problem with sleep in FTC is that it delays the thread from running, this fixes the problem by allowing you to run a function while it is idling.&#x20;

```java
Idler.safeIdle(5,this,{
//update drive
})
```

This will idle for 5 seconds while running //update  drive, allowing it to wait for that amount of seconds and not break anything.
