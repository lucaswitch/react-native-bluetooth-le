package com.rnbluetoothle.bluetooth.bridge;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.os.Build;

import com.rnbluetoothle.bluetooth.BluetoothState;
import com.facebook.react.bridge.ReactApplicationContext;

/**
 * A helper class that parses GATT attributes.
 */
public class JsBluetoothDeviceGatt {

    BluetoothGatt gatt;

    public JsBluetoothDeviceGatt(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    /**
     * Gets the connection state.
     * Possible values:
     * - connected
     * - connecting
     * - disconnected
     * - disconnecting
     */
    public String getConnectionState(ReactApplicationContext context) {
        BluetoothDevice device = this.gatt.getDevice();
        int state;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            BluetoothManager manager = BluetoothState.getBluetoothManager(context);
            state = manager.getConnectionState(device, BluetoothProfile.GATT);
        } else {
            state = this.gatt.getConnectionState(device);
        }

        if (state == BluetoothGatt.STATE_CONNECTED) {
            return "connected";
        } else if (state == BluetoothGatt.STATE_CONNECTING) {
            return "connecting";
        } else if (state == BluetoothGatt.STATE_DISCONNECTED) {
            return "disconnected";
        } else if (state == BluetoothGatt.STATE_DISCONNECTING) {
            return "disconnecting";
        } else {
            throw new Error("Unsupported GATT connection state.");
        }
    }

    /**
     * Gets the connection state.
     * Possible values:
     * - connected
     * - connecting
     * - disconnected
     * - disconnecting
     */
    public static String getConnectionState(ReactApplicationContext context, BluetoothGatt gatt, BluetoothDevice device) {
        int state;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            BluetoothManager manager = BluetoothState.getBluetoothManager(context);
            state = manager.getConnectionState(device, BluetoothProfile.GATT);
        } else {
            state = gatt.getConnectionState(device);
        }

        if (state == BluetoothGatt.STATE_CONNECTED) {
            return "connected";
        } else if (state == BluetoothGatt.STATE_CONNECTING) {
            return "connecting";
        } else if (state == BluetoothGatt.STATE_DISCONNECTED) {
            return "disconnected";
        } else if (state == BluetoothGatt.STATE_DISCONNECTING) {
            return "disconnecting";
        } else {
            throw new Error("Unsupported GATT connection state.");
        }
    }
}
