LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := com.google.android.maps
LOCAL_MODULE_TAGS := optional
LOCAL_STATIC_JAVA_LIBRARIES := osmdroid-android
LOCAL_SRC_FILES := $(call all-java-files-under, mapsv1-compat-osmdroid/src/main/java)
LOCAL_PACKAGE_NAME := com.google.android.maps
LOCAL_CERTIFICATE := platform
LOCAL_SDK_VERSION := current
include $(BUILD_JAVA_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := com.google.android.maps.xml
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_CLASS := ETC
LOCAL_MODULE_PATH := $(TARGET_OUT_ETC)/permissions
LOCAL_SRC_FILES := mapsv1-flashable/src/main/files/system/etc/permissions/$(LOCAL_MODULE)
include $(BUILD_PREBUILT)

