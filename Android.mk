# Exclude Gallery3D from GPU-less devices so that 
# Gallery can be built
ifneq ($(BOARD_HAS_LIMITED_EGL),true)

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := Gallery3D
LOCAL_CERTIFICATE := media

LOCAL_SDK_VERSION := current

include $(BUILD_PACKAGE)

endif
