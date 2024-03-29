package com.rnbluetoothle.bluetooth.receivers;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDevice;
import com.facebook.react.bridge.ReactApplicationContext;

import com.rnbluetoothle.bluetooth.receivers.TransactionReceiver;
import com.rnbluetoothle.bluetooth.bridge.JsEventDispatcher;


/**
 * Deals with a single device connection and disconnection events.
 */
public class ConnectionReceiver extends TransactionReceiver {
    protected String EVENT_ON_CONNECTION_CHANGE = "rnbluetoothle.onConnectionChange/";
    protected String address;

    public ConnectionReceiver(ReactApplicationContext context, String transactionId, String address) {
        super(context, transactionId);
        this.intentActions = new String[]{
                BluetoothDevice.ACTION_ACL_CONNECTED,
                BluetoothDevice.ACTION_ACL_DISCONNECTED
        };
        this.address = address;

        this.EVENT_ON_CONNECTION_CHANGE += transactionId;
    }

    /**
     * On receive any connection or disconnection events.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        BluetoothDevice device = JsBluetoothDevice.getDevice(intent);
        if (this.address.equals(device.getAddress())) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {

                JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);
                JsEventDispatcher.send(this.reactContext, this.EVENT_ON_CONNECTION_CHANGE, jsBluetoothDevice.getMap());
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {

                JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);
                JsEventDispatcher.send(this.reactContext, this.EVENT_ON_CONNECTION_CHANGE, jsBluetoothDevice.getMap());
            }
        }
    }
}
