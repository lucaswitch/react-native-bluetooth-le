package com.rnbluetoothle.bluetooth;

import android.bluetooth.BluetoothAdapter;
import androidx.annotation.Nullable;

public class BluetoothState {
    /**
     * Gets the bluetooth adapter or null if not supported.
     * @return
     */
    @Nullable
    public static BluetoothAdapter getDefaultAdapter() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter;
    }

    /**
     * Checks if device has a bluetooth peripheral and it's available for use.
     * @return boolean
     */
    public static boolean getIsSupported() {
        BluetoothAdapter bluetoothAdapter = getDefaultAdapter();
        return bluetoothAdapter != null;
    }
}