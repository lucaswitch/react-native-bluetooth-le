package com.rnbluetoothle.bluetooth.receivers;

import static android.os.Build.*;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.Log;

import src.main.java.com.rnbluetoothle.bluetooth.JsBluetoothDevice;

/**
 * Deals with a single device connection events.
 */
public class ConnectionReceiver extends com.rnbluetoothle.bluetooth.receivers.DeviceEventsReceiver {
    final String EVENT_ON_CONNECT = "rnbluetoothle.onConnect";
    final String EVENT_ON_DISCONNECT = "rnbluetoothle.onDisconnect";

    public ConnectionReceiver(com.facebook.react.bridge.ReactApplicationContext context, String deviceId) {
        super(context, deviceId);
        this.intentActions = new String[]{
                BluetoothDevice.ACTION_ACL_CONNECTED,
                BluetoothDevice.ACTION_ACL_DISCONNECTED
        };
    }

    /**
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        BluetoothDevice device;
        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice.class);
        } else {
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        }

        final String address = device.getAddress();
        if (address.equals(device.getAddress())) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {

                String eventName = this.EVENT_ON_CONNECT + "/" + address;
                JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);
                this.sendJsModuleEvent(eventName, jsBluetoothDevice.getMap());
                Log.v("Bluetooth", "Remote " + address + "device just connected.");
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {

                String eventName = this.EVENT_ON_DISCONNECT + "/" + address;
                JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);
                this.sendJsModuleEvent(eventName, jsBluetoothDevice.getMap());
                Log.v("Bluetooth", "Remote " + address + "device just disconnected.");
            }
        }
    }
}
