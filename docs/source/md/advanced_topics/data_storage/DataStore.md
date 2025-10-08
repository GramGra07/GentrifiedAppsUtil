# DataStore

DataStore is an addon to DataStorage that allows you to maintain the same data through a robot disconnect. When the robot disconnects, the DataStorage will be lost because it is stored in that instance of a class. To fix this, there are only a couple of lines you need to use.

```Java
DataStorage.initDataStore(); // this initializes the DataStore, this should be in init
DataStorage.readDataStore(); // this reads the DataStore and sets DataStorage to the values it reads
DataStorage.writeDataStore(); // this writes the DataStorage to the DataStore
```

These will all be simple and convenient to use inside your program, you will need to know and decide where to place the functions in order to ensure proper functionality.