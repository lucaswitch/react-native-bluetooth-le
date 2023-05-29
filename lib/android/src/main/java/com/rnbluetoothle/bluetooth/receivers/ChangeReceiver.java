package com.rnbluetoothle.bluetooth.receivers;

import static android.os.Build.VERSION_CODES;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;

import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDevice;

/**
 * Deals with a single device connection changes events.
 */
public class ChangeReceiver extends TransactionReceiver {
    protected String EVENT_ON_CHANGE = "rnbluetoothle.onChange/";
    protected String address;

    public ChangeReceiver(ReactApplicationContext context, String transactionId, String address) {
        super(context, transactionId);
        this.intentActions = new String[]{
                BluetoothDevice.ACTION_CLASS_CHANGED,
                BluetoothDevice.ACTION_NAME_CHANGED
        };
        this.address = address;
        this.EVENT_ON_CHANGE += transactionId;
    }

    /**
     * On receive any device change.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice receivedDevice = jsBluetoothDevice.getDevice(intent);
        if (this.address.equals(receivedDevice.getAddress())) {

            JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);
            this.sendJsModuleEvent(this.EVENT_ON_CHANGE, jsBluetoothDevice.getMap());
        }
    }
}
