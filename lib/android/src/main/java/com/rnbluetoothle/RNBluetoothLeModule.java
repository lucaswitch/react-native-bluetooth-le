package com.rnbluetoothle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import com.rnbluetoothle.bluetooth.BluetoothState;

import com.rnbluetoothle.bluetooth.receivers.DiscoveryReceiver;
import com.rnbluetoothle.bluetooth.receivers.StateReceiver;
import com.rnbluetoothle.bluetooth.receivers.BondsReceiver;
import com.rnbluetoothle.bluetooth.receivers.BondReceiver;
import com.rnbluetoothle.bluetooth.receivers.ChangeReceiver;
import com.rnbluetoothle.bluetooth.receivers.TransactionReceiver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDevice;
import com.rnbluetoothle.bluetooth.bridge.JsDeviceGattCallback;

import org.jetbrains.annotations.Nullable;

public class RNBluetoothLeModule extends NativeReactNativeBluetoothLeSpec {


    public static String NAME = "ReactNativeBluetoothLe";
    private final ReactApplicationContext reactContext;

    /**
     * Hashmap of receivers which each key represents the device id.
     * Listen to bond events of specified device id.
     */
    private HashMap<String, TransactionReceiver> transactionReceivers;
    private HashMap<String, BluetoothGatt> deviceBluetoothGatts;

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
        this.removeListener(eventName);

        // Unregister the event if already exists.
        if (this.transactionReceivers.containsKey(transactionId)) {
            TransactionReceiver receiver = this.transactionReceivers.get(transactionId);
            this.transactionReceivers.remove(transactionId);
            receiver.unregister();
        }

        eventName = eventName.substring(0, eventName.lastIndexOf("/"));
        String transactionId = eventName.substring(eventName.lastIndexOf("/") + 1);
        TransactionReceiver receiver;
        switch (eventName) {

            case "rnbluetoothle.onStateChange":
                receiver = new StateReceiver(this.reactContext, transactionId);
                break;

            case "rnbluetoothle.onDiscovery":
                receiver = new DiscoveryReceiver(this.reactContext, transactionId);
                break;

            case "rnbluetoothle.onBondedDevices":
                receiver = new BondsReceiver(this.reactContext, transactionId);
                break;

            case "rnbluetoothle.onBondChange":
                receiver = new BondReceiver(this.reactContext, transactionId);
                break;

            case "rnbluetoothle.onChange/":
                receiver = new ChangeReceiver(this.reactContext, transactionId);
                break;
            default:
                break;
        }

        if (receiver != null) {
            receiver.register();
            this.transactionReceivers.put(transactionId, receiver);
        }
    }

    /**
     * Remove a JS module event listener.
     */
    @Override
    public void removeListener(String eventName) {
        eventName = eventName.substring(0, eventName.lastIndexOf("/"));
        String transactionId = eventName.substring(eventName.lastIndexOf("/") + 1);

        // Unregister the event if already exists.
        if (this.transactionReceivers.containsKey(transactionId)) {
            TransactionReceiver receiver = this.transactionReceivers.get(transactionId);
            this.transactionReceivers.remove(transactionId);
            receiver.unregister();
        }
    }

    /**
     * Gets the JsDeviceGattCallback of remote device by its id.
     * If the GATT connection is not stablished with the remote device it creates a brand new Gatt connection and if all goes well it returns a BluetoothGatt.
     */
    @Nullable
    private BluetoothGatt getOrSetAndStablishGattConnection(String id) {
        BluetoothDevice device = BluetoothState.getRemoteDevice(id, this.reactContext);
        BluetoothGatt gatt = this.deviceBluetoothGatts.get(id);
        if (gatt == null) {
            JsDeviceGattCallback callback = new JsDeviceGattCallback(this.reactContext);
            gatt = device.connectGatt(this.reactContext, false, callback);
            if (gatt == null) {
                return null;
            }
            this.deviceBluetoothGatts.put(id, gatt);
        }

        return gatt;
    }

    /**
     * Gets whether the device with the id is connected.
     */
    @Override
    public boolean getIsConnected(String id) {
        if (this.getIsSupported()) {
            BluetoothManager bluetoothManager = BluetoothState.getBluetoothManager(this.reactContext);
            List<BluetoothDevice> connectedDevices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
            for (BluetoothDevice device : connectedDevices) {
                if (device.getAddress().equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Connects to device.
     *
     * @param id
     * @param promise
     */
    @Override
    public void connect(String id, String Priority, Promise promise) {
        BluetoothGatt gatt = this.getOrSetAndStablishGattConnection(id);
        if (gatt == null) {
            promise.reject(new Throwable("It was not possible to stablish connection with remote device."));
            return;
        }
        promise.resolve(null);
    }

    /**
     * Removes the current connection with remote device GATT.
     *
     * @param id
     */
    public void disconnect(String id, Promise promise) {
        BluetoothGatt gatt = this.deviceBluetoothGatts.get(id);
        if (gatt == null) {
            // The device is already disconnected.
            promise.resolve(null);
            return;
        }
        try {
            // Try to disconnected from the device.
            gatt.disconnect();
            promise.resolve(null);
        } catch (Exception e) {
            Log.e("Bluetooth", "A exception just happened, details: " + e.toString());
            promise.reject(new Throwable("It was not possible to disconnect from this device."));
        }
    }

    public void initialize() {

    }

    public void invalidate() {

    }
}
