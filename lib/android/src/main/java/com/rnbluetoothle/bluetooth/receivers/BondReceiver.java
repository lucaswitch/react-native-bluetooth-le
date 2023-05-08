package com.rnbluetoothle.bluetooth.receivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

import com.rnbluetoothle.bluetooth.BluetoothState;

import src.main.java.com.rnbluetoothle.bluetooth.JsBluetoothDevice;

/**
 * Responsible to deal with bonding state of device and emit it.
 */
public class BondReceiver extends BroadcastReceiver {

    final String EVENT_ON_BOND = "rnbluetoothle.onBond";
    final String EVENT_ON_UNBOND = "rnbluetoothle.onUnBound";

    /**
     * Target bluetooth device for this receiver.
     */
    BluetoothDevice bluetoothDevice;
    /**
     * React application context.
     */
    ReactApplicationContext reactContext;

    public BondReceiver(ReactApplicationContext context, String deviceId) {
        super();
        BluetoothState bluetoothState = new BluetoothState(context);

        BluetoothAdapter adapter = bluetoothState.getSystemDefaultAdapter();
        this.bluetoothDevice = adapter.getRemoteDevice(deviceId);

        this.reactContext = context;
    }


    /**
     * Sends event back to coupled JS Module.
     */
    private void sendJsModuleEvent(String event, WritableMap map) {
        Log.v("Bluetooth", "Sending JS Module bond event " + event);
        this.reactContext
                .getJSModule(RCTDeviceEventEmitter.class)
                .emit(event, map);
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

    /**
     * Gets the intent filter suitable for bonding events.
     *
     * @return
     */
    private static IntentFilter createIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        return intentFilter;
    }

    /**
     * Register this broadcast receiver.
     */
    public void register() {
        reactContext.registerReceiver(this, createIntentFilter());
        Log.v("Bluetooth", "\"BondReceiver\" registered for device " + this.bluetoothDevice.getAddress());
    }

    /**
     * Unregister this broadcast receiver.
     */
    public void unregister() {
        reactContext.unregisterReceiver(this);
        Log.v("Bluetooth", "\"BondReceiver\" unregistered for device " + this.bluetoothDevice.getAddress());
    }
}
