# Copyright (c) Meta Platforms, Inc. and affiliates.
#
# This source code is licensed under the MIT license found in the
# LICENSE file in the root directory of this source tree.

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := react_codegen_ReactNativeBluetoothLeSpec

LOCAL_C_INCLUDES := $(LOCAL_PATH)

LOCAL_SRC_FILES := $(wildcard $(LOCAL_PATH)/*.cpp) $(wildcard $(LOCAL_PATH)/react/renderer/components/ReactNativeBluetoothLeSpec/*.cpp)
LOCAL_SRC_FILES := $(subst $(LOCAL_PATH)/,,$(LOCAL_SRC_FILES))

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH) $(LOCAL_PATH)/react/renderer/components/ReactNativeBluetoothLeSpec

LOCAL_SHARED_LIBRARIES := libfbjni   libfolly_runtime   libglog   libjsi   libreact_codegen_rncore   libreact_debug   libreact_nativemodule_core   libreact_render_core   libreact_render_debug   libreact_render_graphics   libreact_render_imagemanager   libreact_render_mapbuffer   librrc_image   librrc_view   libturbomodulejsijni   libyoga

LOCAL_CFLAGS := \
  -DLOG_TAG=\"ReactNative\"

LOCAL_CFLAGS += -fexceptions -frtti -std=c++17 -Wall

include $(BUILD_SHARED_LIBRARY)
