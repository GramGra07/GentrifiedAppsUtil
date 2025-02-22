# What is it?

In FTC, voltage matters in almost every aspect, a simple voltage compensation algorithm saves a lot of tuning time by "normalizing" the voltage to get similar performance on all voltages.

# How to use it

To initialize, use the following code:

```java
VoltageCompensator(name,kf);
```
Where name is our expansion or control hub, and kf is your tuned feedforward gain. Start at 1.0 and use that to tune your feedforward gain.

Then, in your main loop, use the following code:

```java
getVoltageCompensatedKf(controlEffort);
```
Where controlEffort is the value you want to compensate for. It will return the value after compensation.