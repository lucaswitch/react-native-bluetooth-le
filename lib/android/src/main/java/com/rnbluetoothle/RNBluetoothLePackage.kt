package com.rnbluetoothle

import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.TurboReactPackage
import com.facebook.react.module.model.ReactModuleInfo
import java.util.Collections
import java.util.List
import java.util.HashMap
import java.util.Map

class RNBluetoothLePackage : TurboReactPackage() {
    override fun getModule(name: String?, reactContext: ReactApplicationContext): NativeModule? =
        null

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider? = null
}