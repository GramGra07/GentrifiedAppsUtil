# CacheManager

The cache manager is a simple way to reduce data reads and writes, basically, you create a function
supplier that will
automatically build and cache the data returned from the function.

This is useful for sensor inputs, where you can use things to detect if it has changed, and if it
has, you should dump
and rebuild the cache.

## How to use it

To use it, you need to create a cache manager with the following parameters:

```java
CacheManager<Type> cacheManager = new CacheManager<>(() -> {
// your function to get the data
    return data;
}); // where 1000 is the time in milliseconds to cache the data
```

This will work with any type of data, as long as your function returns the same type of data.

Then you can use the following methods to manage the data:

```java
cacheManager.loadCache(); // automatically rebuilds the cache if it is empty and returns the data
cacheManager.

dumpCache(); // dumps the cache
cacheManager.

refreshCache(); // dumps and rebuilds the cache
cacheManager.

buildCache(); // builds the cache
```