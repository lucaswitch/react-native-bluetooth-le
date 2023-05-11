package com.rnbluetoothle.bluetooth.receivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.rnbluetoothle.bluetooth.BluetoothState;


/**
 * Responsible to work with ReactContext and deal with BluetoothDevice events.
 */
public class DeviceEventsReceiver extends BroadcastReceiver {

    protected String[] intentActions = {};

    /**
     * Target bluetooth device for this receiver.
     */
    BluetoothDevice bluetoothDevice;
    /**
     * React application context.
     */
    ReactApplicationContext reactContext;

    public DeviceEventsReceiver(com.facebook.react.bridge.ReactApplicationContext context, String deviceId) {
        super();
        BluetoothState bluetoothState = new BluetoothState(context);
        BluetoothAdapter adapter = bluetoothState.getSystemDefaultAdapter();

        this.bluetoothDevice = adapter.getRemoteDevice(deviceId);
        this.reactContext = context;
    }

    /**
     * Register this broadcast receiver.
     */
    public void register() {
        this.reactContext.registerReceiver(this, this.createIntentFilter());
        Log.v("Bluetooth", "\"BondReceiver\" registered for device " + this.bluetoothDevice.getAddress());
    }

    /**
     * Unregister this broadcast receiver.
     */
    public void unregister() {
        this.reactContext.unregisterReceiver(this);
        Log.v("Bluetooth", "\"BondReceiver\" unregistered for device " + this.bluetoothDevice.getAddress());
    }

    /**
     * Sends event back to coupled JS Module.
     */
    protected void sendJsModuleEvent(String event, com.facebook.react.bridge.WritableMap map) {
        Log.v("Bluetooth", "Sending JS Module bond event " + event);
        this.reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(event, map);
    }

    /**
     * Creates the intent filter.
     *
     * @return
     */
    protected IntentFilter createIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        for (int i = 0; i < this.intentActions.length; i++) {
            intentFilter.addAction(this.intentActions[i]);
        }
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
