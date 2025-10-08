# ComparablePair

Once you start to get more advanced, you may want to track last state and new state of a value to
see if the value has
changed.

This is a simple class that will manage this for you.

You can either use the TimeMachinePair or the ComparablePair, they work the same.

```Java
TimeMachinePair<type> name = new TimeMachinePair<>(previous, current);
// then use these methods
name.

setCurrent(current);

name.

setPrevious(previous);

name.

getCurrent();

name.

getPrevious();

name.

isSame();// returns bool

name.

isDifferent();// returns bool
```