package com.rnbluetoothle.bluetooth.bridge;

import android.bluetooth.BluetoothAdapter;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import com.rnbluetoothle.bluetooth.BluetoothState;
import com.facebook.react.bridge.ReactApplicationContext;

/**
 * Responsible to convert to Js Object corresponding to current adapter state.
 */
public class JsBluetoothState {

    /**
     * Gets the current adapter state as map.
     *
     * @return
     */
    public static WritableMap getMap(ReactApplicationContext context) {
        BluetoothState bluetoothState = new BluetoothState(context);
        int state = bluetoothState.getSystemDefaultAdapter().getState();
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