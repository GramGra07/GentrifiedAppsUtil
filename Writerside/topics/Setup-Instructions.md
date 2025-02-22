# Setup Instructions

Start by simply cloning the repository to your local machine in Android Studio.

File -> New -> Project from Version Control -> [https://github.com/GramGra07/HeatseekerSimulator](https://github.com/GramGra07/HeatseekerSimulator)

After waiting for the project to load, you will need to sync the project with Gradle files. This can be done by clicking the "Sync Project with Gradle Files" (elephant with an arrow) button in the top right corner of Android Studio.

After the project is synced, make sure you see main as the run configuration as shown below:

![mainPlay.png](mainPlay.png)

Then, simply wait for it to build and run the project. You should see a window pop up with the simulator running.

**Note: If an error code appears talking about JavaFX, follow the instructions below.**

Only paste the codes for actions as in ```.addWaypoint()``` and not the ```PathBuilder```

## JavaFX Error

Follow these steps to allow it to discover JavaFX:

File -> Project Structure -> Dependencies -> HeatseekerSimulatorJAR -> + -> JAR/AAR -> Path to project \lib\javafx.base.jar 

Then do it again, except with the following jars:

- javafx.controls.jar
- javafx.fxml.jar
- javafx.graphics.jar

After adding all of these, click apply and then OK. Then sync with gradle and try running the project again.