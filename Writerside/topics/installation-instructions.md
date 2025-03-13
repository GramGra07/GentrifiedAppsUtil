# Installation Instructions

**Adding it to your project**

In your `build.gradle` file in the TeamCode module, add the following line to the dependencies:

Copy

Copy

```gradle
dependencies {
    implementation 'com.github.GramGra07:GentrifiedAppsUtil:version'
}
```

Replace version with the latest version here : [https://jitpack.io/#GramGra07/GentrifiedAppsUtil](https://jitpack.io/#GramGra07/GentrifiedAppsUtil)

In your `build.dependencies.gradle` file, add the following to the repositories:

Copy

Copy

```gradle
repositories {
    maven {url = 'https://jitpack.io'}
}
```

Inside your `build.common.gradle` file, add the following to your packaging options in your `android` block:

```gradle
packagingOptions {
    exclude 'META-INF/LICENSE-notice.md'
    exclude 'META-INF/LICENSE.md'
}
```
