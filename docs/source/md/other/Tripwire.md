# Tripwire

Tripwire is an easy way to organize your boolean returns, it allows you to classify it as a tripwire so it is easier to understand.

## How to use it

```Java
Tripwire tripwire = new Tripwire(()->{return true;});
```

Where the `true` is the boolean return. You can also use a lambda function to return a boolean.

```Java
tripwire.isTripped();
```

Will return whether the boolean is true.
