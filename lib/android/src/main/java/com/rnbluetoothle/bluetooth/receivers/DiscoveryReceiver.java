package com.rnbluetoothle.bluetooth.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDevice;

/**
 * Receiver responsible to sending discovery(scan) events.
 */
public class DiscoveryReceiver extends TransactionReceiver {

    final protected String EVENT_ON_DISCOVERY = "rnbluetoothle.onDiscovery";

    public DiscoveryReceiver(ReactApplicationContext context, String transactionId) {
        super(context, transactionId);
        this.intentActions = new String()[]{
            BluetoothDevice.ACTION_FOUND
        }
    }

    /**
     * Receives the discovery intent events.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);
        JsEventDispatcher.send(this.EVENT_ON_DISCOVERY, jsBluetoothDevice.getMap());
    }
}