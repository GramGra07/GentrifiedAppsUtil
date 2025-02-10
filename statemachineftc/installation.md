# Installation

### Adding it to your project

In your `build.gradle` file in the TeamCode module, add the following line to the dependencies:

```
dependencies {
    implementation 'com.github.GramGra07:StateMachineFTC:version'
}
```

Replace version with the latest version here : [https://jitpack.io/#GramGra07/StateMachineFTC](https://jitpack.io/#GramGra07/StateMachineFTC)

In your `build.dependencies.gradle` file, add the following to the repositories:

```
repositories {
    maven {url = 'https://jitpack.io'}
}
```

Inside your `build.common.gradle` file, add the following to your packaging options:

```
packagingOptions {
    exclude 'META-INF/LICENSE-notice.md'
    exclude 'META-INF/LICENSE.md'
}
```
