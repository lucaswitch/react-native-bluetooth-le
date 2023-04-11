package com.rnbluetoothle.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;;
import android.bluetooth.BluetoothAdapter;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableNativeMap;

import android.util.Log;

/**
 * Broadcast receivers that listen state change.
 */
public class BluetoothStateReceiver extends BroadcastReceiver {

    public static final String ACTION_STATE_CHANGE = "BLUETOOTH.ACTION_STATE_CHANGE";

    /**
     * On receive change state data.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("Bluetooth", "\"bluetoothStateReceiver\" receivered a intent.");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Intent dispatchIntent = new Intent(BluetoothStateReceiver.ACTION_STATE_CHANGE);
        if (bluetoothAdapter != null) {
            int state = bluetoothAdapter.getState();
            if (state == BluetoothAdapter.STATE_ON) {
                Log.v("Bluetooth", "Bluetooth state is on.");
                dispatchIntent.putExtra("status", "on");
            } else if (state == BluetoothAdapter.STATE_OFF) {
                Log.v("Bluetooth", "Bluetooth state is off.");
                dispatchIntent.putExtra("status", "off");
            } else {
                return;
            }
        } else {
            Log.v("Bluetooth", "Bluetooth state is not supported.");
            dispatchIntent.putExtra("status", "not_supported");
        }
        context.sendBroadcast(dispatchIntent);
    }

    /**
     * Create a IntentFilter with corresponding receiver supported actions.
     *
     * @return
     */
    public static IntentFilter createIntentFilter() {

        IntentFilter createdIntentFilter = new IntentFilter();
        createdIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        return createdIntentFilter;
    }
}