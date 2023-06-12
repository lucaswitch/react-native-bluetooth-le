package com.rnbluetoothle.bluetooth.bridge;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

public class JsBluetoothDevice {

    /**
     * Last known device intent.
     */
    Intent intent;

    BluetoothDevice device;

    public JsBluetoothDevice(Intent intent) {
        this.intent = intent;
        this.device = JsBluetoothDevice.getDevice(intent);
    }

    public JsBluetoothDevice(BluetoothDevice device) {
        this.device = device;
    }


    /**
     * Gets the intent bluetooth device.
     *
     * @param intent
     * @return
     */
    public static BluetoothDevice getDevice(Intent intent) {
        BluetoothDevice device = null;
        if (intent != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice.class);
            } else {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            }
        }
        return device;
    }

    /**
     * Gets WritableMap containing the correct js module types conversions from java.
     *
     * @return WritableMap
     */
    public WritableMap getMap() {
        WritableMap payload = new WritableNativeMap();
        BluetoothDevice device = this.device;

        String deviceType;
        switch (device.getType()) {
            case BluetoothDevice.DEVICE_TYPE_LE:
                deviceType = "le";
                break;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                deviceType = "dual";
                break;
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                deviceType = "classic";
                break;
            default:
                deviceType = "unknown";
                break;
        }
        short rssi = Short.MIN_VALUE;
        if (this.intent != null) {
            rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
        }
        int dbm = rssi - 101;
        payload.putString("id", device.getAddress());
        payload.putString("name", device.getName());
        payload.putString("address", device.getAddress());
        payload.putInt("rssi", rssi);
        payload.putInt("dbm", dbm);
        payload.putString("type", deviceType);

        // Bounding
        int bondState = device.getBondState();
        if (bondState == BluetoothDevice.BOND_BONDED) {
            payload.putString("bond", "bonded");
        } else if(bondState == BluetoothDevice.BOND_BONDING){
            payload.putString("bond", "bonding");
        } else {
            payload.putString("bond", "none");
        }

        return payload;
    }
}
