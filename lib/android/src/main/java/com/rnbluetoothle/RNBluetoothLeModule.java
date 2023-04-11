package com.rnbluetoothle;

import androidx.annotation.NonNull;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Arguments;

import java.util.Map;
import java.util.HashMap;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rnbluetoothle.NativeReactNativeBluetoothLeSpec;
import com.rnbluetoothle.bluetooth.BluetoothState;
import com.rnbluetoothle.receivers.BluetoothStateReceiver;
import com.rnbluetoothle.receivers.GlobalReceiver;

public class RNBluetoothLeModule extends NativeReactNativeBluetoothLeSpec {

    public static String NAME = "ReactNativeBluetoothLe";
    private ReactApplicationContext reactContext;
    private BluetoothStateReceiver bluetoothStateReceiver;
    /**
     * Receiver that listen to Bluetooth core events in this module.
     * This receiver only listen intents when the host is on responsive state and the UI is visible.
     */
    private GlobalReceiver globalReceiver;

    RNBluetoothLeModule(ReactApplicationContext context) {
        super(context);
        this.reactContext = context;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    /**
     * Gets whether bluetooth is supported.
     *
     * @return
     */
    @Override
    public boolean getIsSupported() {
        return true;
    }

    /**
     * Gets whether bluetooth is supported.
     *
     * @return
     */
    @Override
    public void turnOnIfPossible() {

    }

    /**
     * Gets whether bluetooth is supported.
     *
     * @return
     */
    @Override
    public void turnOffIfPossible() {

    }

    /**
     * Emit event to JS.
     *
     * @param eventName
     * @param args
     */
    public void emitReactContextEvent(String eventName, @Nullable WritableMap args) {
        this.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, args);
    }

    /**
     * Start broadcast receiver related to bluetooth state changing.
     */
    @Override
    public void enableStateChange() {
        this.bluetoothStateReceiver = new BluetoothStateReceiver();

        // Bluetooth state listener receiver.
        this.reactContext.registerReceiver(this.bluetoothStateReceiver, this.bluetoothStateReceiver.createIntentFilter());
        Log.v("Bluetooth", "\"BluetoothStateReceiver\" receiver registered.");

        // Register global listener.
        if (this.globalReceiver == null) {
            this.globalReceiver = new GlobalReceiver(this.reactContext);
        }
        this.reactContext.registerReceiver(this.globalReceiver, this.globalReceiver.createIntentFilter());
        Log.v("Bluetooth", "\"GlobalReceiver\" registered receiver.");
    }

    /**
     * Stops broadcast receiver related to bluetooth state changing.
     */
    @Override
    public void disableStateChange() {
        if (this.bluetoothStateReceiver != null) {

            this.reactContext.unregisterReceiver(this.bluetoothStateReceiver);
            this.bluetoothStateReceiver = null;
            Log.v("Bluetooth", "\"bluetoothStateReceiver\" unregistered receiver.");
        }
    }
}