package com.rnbluetoothle.bluetooth.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;

import src.main.java.com.rnbluetoothle.bluetooth.JsBluetoothDevice;

/**
 * Responsible to deal with bonding state of device and emit it.
 */
public class BondReceiver extends com.rnbluetoothle.bluetooth.receivers.DeviceEventsReceiver {

    final String EVENT_ON_BOND = "rnbluetoothle.onBond";
    final String EVENT_ON_UNBOND = "rnbluetoothle.onUnBound";


    public BondReceiver(ReactApplicationContext context, String deviceId) {
        super(context, deviceId);
        this.intentActions = new String[]{BluetoothDevice.ACTION_BOND_STATE_CHANGED};
    }

    /**
     * On receive any bond event for target BluetoothDevice.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice device;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice.class);
        } else {
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        }

        final String address = this.bluetoothDevice.getAddress();
        if (device != null && address.equals(device.getAddress())) {
            Log.v("Bluetooth", "Bond state for device " + this.bluetoothDevice.getAddress() + " just changed.");

            int bondState = device.getBondState();
            JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);

            if (bondState == BluetoothDevice.BOND_NONE) {
                String eventName = this.EVENT_ON_UNBOND + "/" + bluetoothDevice.getAddress();
                this.sendJsModuleEvent(eventName, jsBluetoothDevice.getMap());
            } else if (bondState == BluetoothDevice.BOND_BONDED) {
                String eventName = this.EVENT_ON_BOND + "/" + bluetoothDevice.getAddress();
                this.sendJsModuleEvent(eventName, jsBluetoothDevice.getMap());
            }
        }
    }
}
