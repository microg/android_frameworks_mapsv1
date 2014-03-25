MapsV1
======
The next generation Maps API v1 implementation, based on osmdroid.

Installation as replacement for Google Maps API v1
--------------------------------------------------
Release builds may be found on the [release page](https://github.com/microg/android_frameworks_mapsv1/releases).
Installation requires a rooted system.

### Android 2.3 - 4.4
Download `com.google.android.maps.zip` and install it from recovery.

#### or

Download `com.google.android.maps.jar` and `com.google.android.maps.xml`, copy them to `/system/framework/com.google.android.maps.jar`, respectively `/system/etc/permissions/com.google.android.maps.xml` and reboot. The following shell commands will do the job:

	adb root && sleep 5 && adb remount
	adb push path/to/com.google.android.maps.jar /system/framework/com.google.android.maps.jar
	adb oush path/to/com.google.android.maps.xml /system/etc/permissions/com.google.android.maps.xml
	adb reboot

Building
--------
To be build with Android Build System using `make com.google.android.maps`.

Alternatively use any build system of your choice by creating an appropriate build file (if it's done well i'll add it via pull request).

### Dependencies
Heavily depends on [osmdroid](https://github.com/osmdroid/osmdroid) as well as its dependency [slf4j](https://github.com/qos-ch/slf4j)

### Building apps against MapsV1
You can build you app against MapsV1 instead of Google Maps.
This will allow you to replace the Google Maps with osmdroid in an existing app without the need to change a line of code.

### As part of a custom ROM
MapsV1 can be build as part of Android when building an Android ROM from source.

Add the repo to your (local) manifest.xml (as well as the dependencies [android_external_osmdroid](https://github.com/microg/android_external_osmdroid) and [android_external_slf4j](https://github.com/microg/android_external_slf4j) and extend the `PRODUCT_PACKAGES` variable with `com.google.android.maps` and `com.google.android.maps.xml`.

Attribution
-----------
This won't be possible without the hard work of the guys at [osmdroid](https://github.com/osmdroid).

License
-------
> This program is free software: you can redistribute it and/or modify
> it under the terms of the GNU General Public License as published by
> the Free Software Foundation, either version 3 of the License, or
> (at your option) any later version.

> This program is distributed in the hope that it will be useful,
> but WITHOUT ANY WARRANTY; without even the implied warranty of
> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
> GNU General Public License for more details.

> You should have received a copy of the GNU General Public License
> along with this program.  If not, see <http://www.gnu.org/licenses/>.
