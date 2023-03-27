package com.rnbluetoothle

import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import java.util.Map
import java.util.HashMap
import com.rnbluetoothle.bluetooth.BluetoothAdapterState
import com.rnbluetoothle.NativeReactNativeBluetoothLeSpec

class RNBluetoothLeModule(reactContext: ReactApplicationContext) : NativeCalculatorSpec(reactContext) {

    companion object {
        const val NAME = "ReactNativeBluetoothLe"
    }

    override fun getName() = NAME

    /**
     * Gets whether bluetooth is supported.
     *
     * @return Boolean
     */
    override fun getIsSupported(): Boolean {
        return BluetoothAdapterState.getIsSupported();
    }

    /**
     * Turns bluetooth adapter On if possible.
     */
    override fun turnOfIfPossible() {
        BluetoothAdapterState.turnOnBluetoothIfPossible()
    }
}
