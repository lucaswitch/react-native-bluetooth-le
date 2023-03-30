package com.rnbluetoothle;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;

import com.rnbluetoothle.NativeReactNativeBluetoothLeSpec;

import com.rnbluetoothle.bluetooth.BluetoothState;
import android.bluetooth.BluetoothAdapter;

public class RNBluetoothLeModule extends NativeReactNativeBluetoothLeSpec {

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
        if (BluetoothState.getIsSupported()) {
            BluetoothAdapter adapter = BluetoothState.getDefaultAdapter();
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT); // Bloqueia a UI?
        }
    }

    /**
     * Gets whether bluetooth is supported.
     *
     * @return
     */
    @Override
    public void turnOffIfPossible() {

    }
}