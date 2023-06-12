package com.rnbluetoothle.bluetooth.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;

import com.facebook.react.bridge.ReactApplicationContext;

import com.rnbluetoothle.bluetooth.BluetoothState;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDevice;
import com.rnbluetoothle.bluetooth.bridge.JsEventDispatcher;
import com.rnbluetoothle.bluetooth.receivers.TransactionReceiver;

import android.util.Log;

/**
 * Receiver responsible to sending discovery(scan) events.
 */
public class DiscoveryReceiver extends TransactionReceiver {

    protected String EVENT_ON_DISCOVERY = "rnbluetoothle.onDiscovery/";

    public DiscoveryReceiver(ReactApplicationContext context, String transactionId) {
        super(context, transactionId);
        this.intentActions = new String[]{
                BluetoothDevice.ACTION_FOUND
        };
        this.EVENT_ON_DISCOVERY = this.EVENT_ON_DISCOVERY + transactionId;
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
        JsEventDispatcher.send(this.reactContext, this.EVENT_ON_DISCOVERY, jsBluetoothDevice.getMap());
    }

    /**
     * Actually start discovery too.
     */
    @Override
    public void register() {
        super.register();
        BluetoothAdapter bluetoothAdapter = BluetoothState.getSystemDefaultAdapter(this.reactContext);
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.startDiscovery()) {
                Log.v("Bluetooth", "Started bluetooth discovery.");
            }
        }
    }

    /**
     * Actually stop discovery too.
     */
    @Override
    public void unregister() {
        super.register();
        BluetoothAdapter bluetoothAdapter = BluetoothState.getSystemDefaultAdapter(this.reactContext);
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.cancelDiscovery()) {
                Log.v("Bluetooth", "Stopped bluetooth discovery.");
            }
        }
    }
}
