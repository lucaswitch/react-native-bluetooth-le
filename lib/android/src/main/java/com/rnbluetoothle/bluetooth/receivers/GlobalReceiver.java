package com.rnbluetoothle.bluetooth.receivers;

import com.rnbluetoothle.bluetooth.BluetoothState;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;

import src.main.java.com.rnbluetoothle.bluetooth.JsBluetoothDevice;

/**
 * Global receiver responsible to receive this library broadcast messages and emit to ReactNativeContext.
 */
public class GlobalReceiver extends BroadcastReceiver {

    final String EVENT_ON_STATE_CHANGE = "rnbluetoothle.onStateChange";
    final String EVENT_ON_DISCOVERY = "rnbluetoothle.onDiscovery";

    private final BluetoothState bluetoothState;
    private final ReactApplicationContext reactContext;
    private final HashMap<String, String> enabledEvents = new HashMap<String, String>();

    public GlobalReceiver(ReactApplicationContext context) {
        super();

        this.bluetoothState = new BluetoothState(context);
        this.reactContext = context;
    }

    /**
     * Sends event back to coupled JS Module.
     */
    private void sendJsModuleEvent(String event, WritableMap map) {
        Log.v("Bluetooth", "Sending JS Module event " + event);
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(event, map);
    }

    /**
     * Create a IntentFilter with corresponding receiver supported actions.
     *
     * @return
     */
    public static IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
        return filter;
    }

    /**
     * Gets if event is enabled
     */
    public boolean getIsEventEnabled(String event) {
        return this.enabledEvents.containsKey(event);
    }

    /**
     * Enable events.
     */
    public void enableEvent(String event) {
        if (event.equals(this.EVENT_ON_STATE_CHANGE)) {
            // State change
            enabledEvents.put(this.EVENT_ON_STATE_CHANGE, event);
        } else if (event.equals(this.EVENT_ON_DISCOVERY)) {
            // Discovery
            BluetoothAdapter adapter = this.bluetoothState.getSystemDefaultAdapter();
            if (adapter != null) {
                if (adapter.startDiscovery()) {
                    Log.v("Bluetooth", "Bluetooth discovery started.");
                    enabledEvents.put(this.EVENT_ON_DISCOVERY, event);
                    return;
                }

                Log.v("Bluetooth", "Bluetooth discovery could not be started.");
            } else {
                Log.v("Bluetooth", "No system bluetooth adapter found, could not start discovery.");
            }
        }
    }

    /**
     * Disable event.
     */
    public void disableEvent(String event) {
        if (enabledEvents.size() > 0) {
            if (event.equals(this.EVENT_ON_DISCOVERY)) {
                // Discovery
                if (enabledEvents.containsKey(this.EVENT_ON_DISCOVERY)) {
                    BluetoothAdapter adapter = this.bluetoothState.getSystemDefaultAdapter();
                    if (adapter != null) {
                        if (adapter.isDiscovering()) {
                            adapter.cancelDiscovery();
                            Log.v("Bluetooth", "Bluetooth discovery stopped.");
                        }
                    }
                }
            }
            enabledEvents.remove(event);
        }
    }

    /**
     * Gets events count.
     */
    public int getEventsCount() {
        return enabledEvents.size();
    }

    /**
     * Register this broadcast receiver.
     */
    public void register() {
        reactContext.registerReceiver(this, createIntentFilter());
        Log.v("Bluetooth", "\"GlobalReceiver\" registered.");
    }

    /**
     * Unregister this broadcast receiver.
     */
    public void unregister() {
        reactContext.unregisterReceiver(this);
    }

    /**
     * On receive a Intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (this.getEventsCount() > 0) {
            final String className = context.getClass().getName();
            String action = intent.getAction();
            Log.v("Bluetooth", className + " received a intent: " + action);

            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED: // Bluetooth state just changed to On or Off.
                    if (this.getIsEventEnabled(this.EVENT_ON_STATE_CHANGE)) {
                        WritableMap statusChangeMapping = createNativeMapForBluetoothStateChangeIntent(intent);
                        if (statusChangeMapping != null) {
                            this.sendJsModuleEvent(this.EVENT_ON_STATE_CHANGE, statusChangeMapping);
                        }
                    }
                    break;
                case BluetoothDevice.ACTION_FOUND: // Bluetooth device just discovered
                    if (this.getIsEventEnabled(this.EVENT_ON_DISCOVERY)) {
                        this.sendJsModuleEvent(this.EVENT_ON_DISCOVERY, createNativeMapForBluetoothDeviceDiscoveryIntent(intent));
                    }
                    break;
                default:
                    Log.v("Bluetooth", className + " received a intent: " + action + " and it can not be supported.");
                    break;
            }
        }
    }


    /**
     * Create WritableMap accordingly to "ACTION_STATE_CHANGED" intent.
     */
    @Nullable
    private WritableMap createNativeMapForBluetoothStateChangeIntent(Intent intent) {
        WritableMap payload = Arguments.createMap();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        int state = bluetoothAdapter.getState();
        if (state == BluetoothAdapter.STATE_OFF) {
            payload.putString("status", "off");
            return payload;
        } else if (state == BluetoothAdapter.STATE_ON) {
            payload.putString("status", "on");
            return payload;
        }

        return null;
    }

    /**
     * Create WritableMap accordingly to "ACTION_FOUND" intent.
     */
    @Nullable
    private WritableMap createNativeMapForBluetoothDeviceDiscoveryIntent(Intent intent) {
        JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);
        return jsBluetoothDevice.getMap();
    }
}
