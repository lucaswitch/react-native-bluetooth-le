package com.rnbluetoothle.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;

public class BluetoothState {

    private final BluetoothAdapter adapter;

    public BluetoothState(ReactApplicationContext context) {
        this.adapter = getSystemDefaultAdapter(context);
    }

    /**
     * Gets the system default adapter.
     *
     * @return
     */
    @Nullable
    public BluetoothAdapter getSystemDefaultAdapter() {
        return this.adapter;
    }

    /**
     * Gets the default BluetoothManager.
     *
     * @return
     */
    public static BluetoothManager getBluetoothManager(ReactApplicationContext context) {
        return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    /**
     * Gets the default bluetooth adapter, it must support
     * multipleAdvertisementSupported too.
     */
    @Nullable
    public static BluetoothAdapter getSystemDefaultAdapter(ReactApplicationContext context) {
        BluetoothAdapter adapter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 or above.
            BluetoothManager bluetoothManager = BluetoothState.getBluetoothManager(context);
            if (bluetoothManager != null) {
                adapter = bluetoothManager.getAdapter();
            }
        } else {
            adapter = BluetoothAdapter.getDefaultAdapter();
        }
        return adapter;
    }

    /**
     * Gets the remote device instance.
     */
    @Nullable
    public static BluetoothDevice getRemoteDevice(String id, ReactApplicationContext context) {
        BluetoothState bluetoothState = new BluetoothState(context);
        BluetoothAdapter adapter = bluetoothState.getSystemDefaultAdapter();
        if (adapter != null) {
            return adapter.getRemoteDevice(id);
        }
        return null;
    }

    /**
     * Checks if device has a bluetooth peripheral and it's available for use.
     *
     * @return boolean
     */
    public static boolean getIsSupported(ReactApplicationContext context) {
        return getSystemDefaultAdapter(context) != null;
    }
}