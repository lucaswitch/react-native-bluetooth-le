package com.rnbluetoothle.bluetooth.receivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDevice;
import com.rnbluetoothle.bluetooth.bridge.JsEventDispatcher;

import com.rnbluetoothle.bluetooth.BluetoothState;

import java.util.Set;

/**
 * Responsible to deal with bonding state of device and emit it.
 */
public class BondsReceiver extends TransactionReceiver {
    protected String EVENT_ON_BONDED_DEVICES = "rnbluetoothle.onBondedDevices/";

    public BondsReceiver(ReactApplicationContext context, String transactionId) {
        super(context, transactionId);
        this.intentActions = new String[]{BluetoothDevice.ACTION_BOND_STATE_CHANGED};

        this.EVENT_ON_BONDED_DEVICES += transactionId;
    }

    /**
     * On receive any bond event for target BluetoothDevice.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothState bluetoothState = new BluetoothState(this.reactContext);
        BluetoothAdapter adapter = bluetoothState.getSystemDefaultAdapter();

        WritableArray jsBondedDevices = Arguments.createArray();
        Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();
        for (BluetoothDevice device : bondedDevices) {
            JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);
            jsBondedDevices.pushMap(jsBluetoothDevice.getMap());
        }

        JsEventDispatcher.send(this.EVENT_ON_BONDED_DEVICES, jsBondedDevices);
    }
}
