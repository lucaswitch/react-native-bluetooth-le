package com.rnbluetoothle.bluetooth.bridge;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableNativeMap;

/**
 * Responsible to convert to Js Object corresponding to current adapter state.
 */
public class JsBluetoothState {

    /**
     * Gets the current adapter state as map.
     *
     * @return
     */
    public static WritableMap getMap() {
        int state = bluetoothAdapter.getState();
        WritableMap map = Arguments.createMap();

        switch (state) {
            case BluetoothAdapter.STATE_ON:
                map.putString("status", "on");
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                map.putString("status", "turning_on");
                break;
            case BluetoothAdapter.STATE_OFF:
                map.putString("status", "off");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                map.putString("status", "turning_off");
                break;
            default:
                map.putString("status", "unknown");
                break;
        }
        return map;
    }
}