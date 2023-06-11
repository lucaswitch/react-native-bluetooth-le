package com.rnbluetoothle;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import com.facebook.react.TurboReactPackage;
import com.facebook.react.module.model.ReactModuleInfo;
import com.rnbluetoothle.RNBluetoothLeModule;

import java.util.HashMap;
import java.util.Map;

public class RNBluetoothLePackage extends TurboReactPackage {

    @Nullable
    @Override
    public NativeModule getModule(String name, ReactApplicationContext reactContext) {
        if (name.equals(RNBluetoothLeModule.NAME)) {
            return new RNBluetoothLeModule(reactContext);
        } else {
            return null;
        }
    }

    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return () -> {
            final Map<String, ReactModuleInfo> moduleInfos = new HashMap<>();
            moduleInfos.put(
                    RNBluetoothLeModule.NAME,
                    new ReactModuleInfo(
                            RNBluetoothLeModule.NAME,
                            RNBluetoothLeModule.NAME,
                            false,
                            false,
                            true,
                            false,
                            true
                    ));
            return moduleInfos;
        };

    }
}