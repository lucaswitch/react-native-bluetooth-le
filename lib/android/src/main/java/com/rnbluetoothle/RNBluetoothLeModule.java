package com.rnbluetoothle;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.util.Map;
import java.util.HashMap;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;

import com.rnbluetoothle.NativeReactNativeBluetoothLeSpec;
import com.rnbluetoothle.bluetooth.BluetoothState;
import com.rnbluetoothle.receivers.BluetoothStateReceiver;

public class RNBluetoothLeModule extends NativeReactNativeBluetoothLeSpec {

    public BluetoothStateReceiver bluetoothStateReceiver;

    /**
     * Receiver that listen to Bluetooth core events in this module.
     * This receiver only listen intents when the host is on responsive state and the UI is visible.
     */
    private BroadcastReceiver lazyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothStateReceiver.ACTION_STATE_CHANGE)) {
                bluetoothStateReceiver.executeJsCallbackWithIntent(intent);
            }
        }
    };

    public static String NAME = "ReactNativeBluetoothLe";

    RNBluetoothLeModule(ReactApplicationContext context) {
        super(context);
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
     * Start broadcast receiver related to bluetooth state changing.
     */
    @Override
    public void subscribeStateChange(Callback jsCallback) {
        this.bluetoothStateReceiver = new BluetoothStateReceiver(jsCallback);
        // Intent filters.
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothStateReceiver.ACTION_STATE_CHANGE);

        this.getCurrentActivity().registerReceiver(bluetoothStateReceiver, intentFilter);
    }

    /**
     * Stops broadcast receiver related to bluetooth state changing.
     */
    @Override
    public void unsubscribeStateChange() {
        if (this.bluetoothStateReceiver != null) {
            this.getCurrentActivity().unregisterReceiver(bluetoothStateReceiver);
            this.bluetoothStateReceiver = null;
        }
    }
}