# QooApp OpenSDK demo for Android

This is a demo for using QooApp OpenSDK.

Please visit the [QooApp OpenSDK official website](https://open.qoo-app.com/website/en/docs/open_sdk_guide) for more informoation!

Please replace the AAR package in the project with the latest SDK file

### 1. The package name must end with **".qooapp"**


### 2. If your targetSdkVersion >= 30, you must add the following code to the AndroidManifest.xml

``` xml
<queries>
	<package android:name="com.qooapp.qoohelper" />
</queries>
```
