package com.rnbluetoothle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.Arguments;

import com.rnbluetoothle.bluetooth.BluetoothState;
import com.rnbluetoothle.bluetooth.receivers.BondReceiver;
import com.rnbluetoothle.bluetooth.receivers.GlobalReceiver;
import com.facebook.react.bridge.ReadableMap;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import src.main.java.com.rnbluetoothle.bluetooth.JsBluetoothDevice;

public class RNBluetoothLeModule extends NativeReactNativeBluetoothLeSpec {


    public static String NAME = "ReactNativeBluetoothLe";
    private final ReactApplicationContext reactContext;

    /**
     * Receiver that listen to Bluetooth core events in this module.
     * This receiver only listen intents when the host is on responsive state and the UI is visible.
     */
    private GlobalReceiver globalReceiver;

    /**
     * Hashmap of receivers which each key represents the device id.
     * Listen to bond events of specified device id.
     */
    private HashMap<String, BondReceiver> deviceBondReceivers;

    RNBluetoothLeModule(ReactApplicationContext context) {
        super(context);
        this.deviceBondReceivers = new HashMap<>();
        this.reactContext = context;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    /**
     * Gets whether bluetooth is supported.
     */
    @Override
    public boolean getIsSupported() {
        return BluetoothState.getIsSupported(this.reactContext);
    }

    /**
     * Gets if system bluetooth adapter is enabled.
     */
    @Override
    public boolean getIsEnabled() {
        BluetoothAdapter adapter = BluetoothState.getSystemDefaultAdapter(this.reactContext);
        if (adapter != null) {
            return adapter.isEnabled();
        }
        return false;
    }

    /**
     * Gets if system location is enabled.
     */
    @Override
    public boolean getIsLocationEnabled() {
        LocationManager location = (LocationManager) this.reactContext.getSystemService(Context.LOCATION_SERVICE);
        try {
            return location.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e("Bluetooth", "Location could not be verified, error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Gets system bluetooth adapter name.
     */
    @Override
    public String getAdapterName() {
        BluetoothAdapter adapter = BluetoothState.getSystemDefaultAdapter(this.reactContext);
        if (adapter != null) {
            return adapter.getName();
        }
        return "";
    }

    /**
     * Gets system bluetooth adapter address.
     */
    @Override
    public String getAdapterAddress() {
        BluetoothAdapter adapter = BluetoothState.getSystemDefaultAdapter(this.reactContext);
        if (adapter != null) {
            return adapter.getAddress();
        }
        return "";
    }

    /**
     * Gets whether the given device is bonded.
     *
     * @return boolean
     */
    @Override
    public boolean getIsBonded(String id) {
        if (!this.getIsSupported()) {
            return false;
        }
        if (id.length() == 0) {
            return false;
        }

        BluetoothState bluetoothState = new BluetoothState(this.reactContext);
        BluetoothAdapter adapter = bluetoothState.getSystemDefaultAdapter();
        if (adapter != null) {
            BluetoothDevice bluetoothDevice = adapter.getRemoteDevice(id);
            return adapter.getBondedDevices().contains(bluetoothDevice);
        }
        return false;
    }

    /**
     * Creates a remote device bound within the bluetooth adapter.
     * If it successfully started returns true.
     * Returns empty string if is successfull.
     */
    @Override
    public String bond(String id) {
        if (this.getIsEnabled()) {
            BluetoothState bluetoothState = new BluetoothState(this.reactContext);
            BluetoothAdapter adapter = bluetoothState.getSystemDefaultAdapter();
            BluetoothDevice device = adapter.getRemoteDevice(id);
            if (this.getIsBonded(id)) {
                Log.e("Bluetooth", "Bluetooth remote device " + id + " is already bonded.");
                return "";
            } else if (device.createBond()) {
                Log.e("Bluetooth", "Bluetooth remote device " + id + " must be successfully bonded.");
                return "";
            }
            String errorMessage = "Could not bond the remote device with address " + id + " because bluetooth is not supported or enabled on device bluetooth adapter.";
            Log.e("Bluetooth", errorMessage);
            return errorMessage;
        }

        String errorMessage = "Could not bond the remote device with address " + id + " because bluetooth is not supported or enabled on device bluetooth adapter.";
        Log.e("Bluetooth", errorMessage);
        return errorMessage;
    }

    /**
     * Remove a remote device bound within the bluetooth adapter.
     * If it successfully remove returns true.
     * Returns empty string if is successfull.
     */
    @Override
    public String unBond(String id) {
        if (this.getIsEnabled()) {
            if (!this.getIsBonded(id)) {
                Log.e("Bluetooth", "Bluetooth remote device " + id + " is already bonded.");
                return "";
            }
            BluetoothState bluetoothState = new BluetoothState(this.reactContext);
            BluetoothAdapter adapter = bluetoothState.getSystemDefaultAdapter();
            BluetoothDevice device = adapter.getRemoteDevice(id);

            // The removeBond method is hidden on Sdk Api, we need to use a small tricky to make this "private" method accessible.
            try {
                Method hiddenMethod = device.getClass().getMethod("removeBond", (Class[]) null);
                hiddenMethod.invoke(device, (Object[]) null);
                Log.e("Bluetooth", "Bluetooth remote device " + id + " must be successfully unbonded.");
                return "";
            } catch (Exception e) {
                String errorMessage = "Could not unpair remote device, reason: " + e.getMessage();
                Log.e("Bluetooth", errorMessage);
                return "";
            }
        }

        String errorMessage = "Could not unbond the remote device with address " + id + " because bluetooth is not supported or enabled on device bluetooth adapter.";
        Log.e("Bluetooth", errorMessage);
        return errorMessage;
    }

    /**
     * Gets the current bonded devices.
     */
    @Override
    public WritableArray getBondedDevices() {
        BluetoothState state = new BluetoothState(this.reactContext);
        BluetoothAdapter adapter = state.getSystemDefaultAdapter();
        WritableArray jsBluetoothDevices = Arguments.createArray();

        if (adapter != null) {
            Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();

            for (BluetoothDevice device : bondedDevices) {
                jsBluetoothDevices.pushMap((new JsBluetoothDevice(device)).getMap());
            }
            return jsBluetoothDevices;
        }
        return jsBluetoothDevices;
    }

    /**
     * Gets whether bluetooth is supported.
     *
     * @return
     */
    @Override
    public void turnOnIfPossible() {
    }

    /**
     * Gets whether bluetooth is supported.
     *
     * @return
     */
    @Override
    public void turnOffIfPossible() {
    }

    /**
     * Starts broadcast receivers.
     */
    private void registerGlobalBroadcast() {
        // Register global listener.
        if (this.globalReceiver == null) {
            this.globalReceiver = new GlobalReceiver(this.reactContext);
            reactContext.registerReceiver(this.globalReceiver, GlobalReceiver.createIntentFilter());
            Log.v("Bluetooth", "\"GlobalReceiver\" registered receiver.");
        }
    }

    /**
     * Stops broadcast receiver.
     */
    private void unregisterGlobalBroadcast() {
        if (globalReceiver != null) {
            reactContext.unregisterReceiver(globalReceiver);
            globalReceiver = null;
        }
    }

    /**
     * Add a JS module event listener.
     */
    @Override
    public void addListener(String eventName) {
        if (eventName.startsWith("rnbluetoothle.onBond/") || eventName.startsWith("rnbluetoothle.onUnBound/")) {

            // Register bond event only if does not exists.
            String deviceId = eventName.substring(eventName.lastIndexOf("/") + 1);
            if (!this.deviceBondReceivers.containsKey(deviceId)) {
                BondReceiver bondReceiver = new BondReceiver(this.reactContext, deviceId);
                bondReceiver.register();
                this.deviceBondReceivers.put(deviceId, bondReceiver);
            }

        } else {
            Log.v("Bluetooth", "Received addListener on event: " + eventName);
            registerGlobalBroadcast();
            globalReceiver.enableEvent(eventName);
        }
    }

    /**
     * Remove a JS module event listener.
     */
    @Override
    public void removeListener(String eventName) {

        if (eventName.startsWith("rnbluetoothle.onBond/") || eventName.startsWith("rnbluetoothle.onUnBound/")) {

            // Unregister if device has previously registered.
            // Each event is distinguished by a slash "/" followed by device id.
            String deviceId = eventName.substring(eventName.lastIndexOf("/") + 1);
            BondReceiver bondReceiver = this.deviceBondReceivers.get(deviceId);
            if (bondReceiver != null) {
                bondReceiver.unregister();
                this.deviceBondReceivers.remove(deviceId);
            }

        } else {
            Log.v("Bluetooth", "Received removeListener on event: " + eventName + " global receiver is taking care of it.");
            globalReceiver.disableEvent(eventName);
            if (globalReceiver.getEventsCount() == 0) {
                unregisterGlobalBroadcast();
            }
        }
    }

    public void initialize() {

    }

    public void invalidate() {

    }
}
