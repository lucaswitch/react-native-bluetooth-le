package com.rnbluetoothle.bluetooth.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;

import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDevice;
import com.rnbluetoothle.bluetooth.bridge.JsEventDispatcher;
import com.rnbluetoothle.bluetooth.receivers.TransactionReceiver;


/**
 * Responsible to deal with bonding state of device and emit it.
 */
public class BondReceiver extends TransactionReceiver {

    protected String EVENT_ON_BOND_CHANGE = "rnbluetoothle.onBondChange/";

    protected String address;

    public BondReceiver(ReactApplicationContext context, String transactionId, String address) {
        super(context, transactionId);
        this.intentActions = new String[]{BluetoothDevice.ACTION_BOND_STATE_CHANGED};

        this.address = address;
        this.EVENT_ON_BOND_CHANGE += transactionId;
    }

    /**
     * On receive any bond event for target BluetoothDevice.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice receivedDevice = JsBluetoothDevice.getDevice(intent);
        if (this.address.equals(receivedDevice.getAddress())) {
            JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);
            WritableMap data = jsBluetoothDevice.getMap();

            JsEventDispatcher.send(this.reactContext, this.EVENT_ON_BOND_CHANGE, data);
        }
    }
}
