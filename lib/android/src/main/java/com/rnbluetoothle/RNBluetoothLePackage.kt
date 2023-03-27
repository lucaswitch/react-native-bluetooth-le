package com.rnbluetoothle

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.rtncalculator.NativeCalculatorSpec

class RNBluetoothLePackage : TurboReactPackage() {
    override fun getModule(name: String?, reactContext: ReactApplicationContext): NativeModule? =
        null

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider? = null
}