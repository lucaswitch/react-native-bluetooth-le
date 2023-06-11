package com.rnbluetoothle.bluetooth.bridge;

import android.bluetooth.BluetoothProfile;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

/**
 * Represents Bluetooth Gatt connection state.
 */
public class JsBluetoothConnectionState {

    protected int state;
    protected int prevState;

    public JsBluetoothConnectionState(int state, int prevState) {
        this.state = state;
        this.prevState = prevState;
    }

    /**
     * Gets String state based on int BluetoothProfile state.
     *
     * @param state
     */
    protected String getStringState(int state) {
        switch (state) {
            case BluetoothProfile.STATE_CONNECTED:
                return "connected";
            case BluetoothProfile.STATE_CONNECTING:
                return "connecting";
            case BluetoothProfile.STATE_DISCONNECTED:
                return "disconnected";
            case BluetoothProfile.STATE_DISCONNECTING:
                return "disconnecting";
            default:
                return "unknown";
        }
    }

    /**
     * Gets the JS representation of current status.
     */
    public WritableMap getMap() {
        WritableMap map = Arguments.createMap();
        map.putString("status", this.getStringState(this.state));
        map.putString("prev_status", this.getStringState(this.prevState));
        return map;
    }
}
