MapsV1
======
The next generation Maps API v1 implementation, based on osmdroid.

Installation as system library
------------------------------
MapsV1 can be used on any rooted Android system to replace the Google Maps API v1 with an OpenStreetMap based implementation.

Release builds may be found on the [release page](https://github.com/microg/android_frameworks_mapsv1/releases).

### Android 2.3 - 4.4
Download `com.google.android.maps.zip` and install it from recovery.

#### or

Download `com.google.android.maps.jar` and `com.google.android.maps.xml`, copy them to `/system/framework/com.google.android.maps.jar`, respectively `/system/etc/permissions/com.google.android.maps.xml` 
and reboot. The following shell commands will do the job:

    adb root && sleep 5 && adb remount
    adb push path/to/com.google.android.maps.jar /system/framework/com.google.android.maps.jar
    adb push path/to/com.google.android.maps.xml /system/etc/permissions/com.google.android.maps.xml
    adb reboot

Building
--------
To be build with Android Build System using `make com.google.android.maps`.

Alternatively use any build system of your choice by creating an appropriate build file (if it's done well i'll add it via pull request).

### Dependencies
Heavily depends on [osmdroid](https://github.com/osmdroid/osmdroid) as well as its dependency [slf4j](https://github.com/qos-ch/slf4j)

### Building apps against MapsV1
You can build your app against MapsV1 instead of Google Maps.
This will allow you to replace Google Maps with osmdroid in an existing app without the need to change a line of code.

### As part of a custom ROM
MapsV1 can be build as part of Android when building an Android ROM from source.

Add the repo to your (local) manifest.xml (as well as the dependencies [android_external_osmdroid](https://github.com/microg/android_external_osmdroid) and 
[android_external_slf4j](https://github.com/microg/android_external_slf4j)) and extend the `PRODUCT_PACKAGES` variable with `com.google.android.maps` and `com.google.android.maps.xml`.

Attribution
-----------
This won't be possible without the hard work of the guys at [osmdroid](https://github.com/osmdroid).


License
-------
    Copyright 2014-2015 μg Project Team

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
