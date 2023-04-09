package com.rnbluetoothle.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableNativeMap;

/**
 * Broadcast receivers that listen state change.
 */
public class BluetoothStateReceiver extends BroadcastReceiver {

    public static final String ACTION_STATE_CHANGE = "BLUETOOTH.ACTION_STATE_CHANGE";

    public final Callback jsCallback;

    public BluetoothStateReceiver(Callback callback) {
        jsCallback = callback;
    }

    /**
     * On receive change state data.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Intent dispatchIntent = new Intent(BluetoothStateReceiver.ACTION_STATE_CHANGE);
        if (bluetoothAdapter != null) {
            int state = bluetoothAdapter.getState();
            if (state == BluetoothAdapter.STATE_ON) {
                dispatchIntent.putExtra("status", "on");
            } else if (state == BluetoothAdapter.STATE_OFF) {
                dispatchIntent.putExtra("status", "off");
            } else {
                return;
            }
        } else {
            dispatchIntent.putExtra("status", "not_supported");
        }
        context.sendBroadcast(dispatchIntent);
    }

    /**
     * Executes the callback accordingly the intent.
     *
     * @param intent
     */
    public void executeJsCallbackWithIntent(Intent intent) {
        WritableNativeMap map = new WritableNativeMap();
        map.putString("status", intent.getStringExtra("status"));
        jsCallback.invoke(map);
    }
}