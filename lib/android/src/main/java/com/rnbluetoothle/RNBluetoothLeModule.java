package com.rnbluetoothle;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
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
import com.facebook.react.bridge.ReadableArray;

import com.rnbluetoothle.bluetooth.BluetoothState;
import com.rnbluetoothle.bluetooth.GattHelper;
import com.rnbluetoothle.bluetooth.receivers.TransactionReceiver;
import com.rnbluetoothle.bluetooth.receivers.DiscoveryReceiver;
import com.rnbluetoothle.bluetooth.receivers.BondsReceiver;
import com.rnbluetoothle.bluetooth.receivers.BondReceiver;
import com.rnbluetoothle.bluetooth.receivers.ChangeReceiver;
import com.rnbluetoothle.bluetooth.receivers.AdapterStateChangeReceiver;

import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDevice;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDeviceGattCallback;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDeviceService;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDeviceServiceCharacteristic;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Module that exposes bluetooth api.
 */
public class RNBluetoothLeModule extends NativeReactNativeBluetoothLeSpec {

    public static String NAME = "ReactNativeBluetoothLe";
    private final ReactApplicationContext reactContext;

    /**
     * Hashmap of receivers which each key represents the device id.
     * Listen to bond events of specified device id.
     */
    private HashMap<String, TransactionReceiver> transactionReceivers;
    private HashMap<String, BluetoothGatt> deviceBluetoothGatts;
    private HashMap<String, JsBluetoothDeviceGattCallback> deviceBluetoothGattCallbacks;

    RNBluetoothLeModule(ReactApplicationContext context) {
        super(context);

        this.reactContext = context;
        this.transactionReceivers = new HashMap<>();
        this.deviceBluetoothGatts = new HashMap<String, BluetoothGatt>();
        this.deviceBluetoothGattCallbacks = new HashMap<String, JsBluetoothDeviceGattCallback>();
    }

    
    /**
     * Gets the module name.
     *
     * @return
     */
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
            String errorMessage = "Could not bond the remote device with address " + id
                    + " because bluetooth is not supported or enabled on device bluetooth adapter.";
            Log.e("Bluetooth", errorMessage);
            return errorMessage;
        }

        String errorMessage = "Could not bond the remote device with address " + id
                + " because bluetooth is not supported or enabled on device bluetooth adapter.";
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

            // The removeBond method is hidden on Sdk Api, we need to use a small tricky to
            // make this "private" method accessible.
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

        String errorMessage = "Could not unbond the remote device with address " + id
                + " because bluetooth is not supported or enabled on device bluetooth adapter.";
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
     * Gets the characteristic.
     *
     * @return
     */
    @Override
    public WritableArray getCharacteristic(String address, String serviceUUID, String characteristicUUID) {
        WritableArray jsCharacteristics = Arguments.createArray();
        BluetoothGatt gatt = this.getGattConnection(address);

        if (gatt != null) {
            BluetoothGattCharacteristic characteristic = GattHelper.getCharacteristic(gatt, serviceUUID,
                    characteristicUUID);
            if (characteristic != null) {
                jsCharacteristics.pushMap(JsBluetoothDeviceServiceCharacteristic.getMap(characteristic));
            }
        }

        return jsCharacteristics;
    }

    /**
     * Gets the service.
     *
     * @param address
     * @param serviceUUID
     * @return
     */
    @Override
    public WritableArray getService(String address, String serviceUUID) {
        WritableArray jsServices = Arguments.createArray();
        BluetoothGatt gatt = this.getGattConnection(address);

        if (gatt != null) {
            BluetoothGattService service = GattHelper.getService(gatt, serviceUUID);
            if (service != null) {
                jsServices.pushMap(JsBluetoothDeviceService.getMap(service));
            }
        }

        return jsServices;
    }

    /**
     * Gets all services.
     *
     * @param address
     * @param serviceUUID
     * @return
     */
    @Override
    public WritableArray getServices(String address) {
        BluetoothGatt gatt = this.getGattConnection(address);

        WritableArray jsServices = Arguments.createArray();
        if (gatt != null) {
            List<BluetoothGattService> services = GattHelper.getAllServices(gatt);
            if (services != null && services.size() > 0) {
                JsBluetoothDeviceService.getMap(services);
            }
        }

        return jsServices;
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
     * Add a JS module event listener.
     */
    @Override
    public void addListener(String eventName) {
        String[] properties = this.getEventProperties(eventName);
        eventName = properties[0];
        String transactionId = null;
        String address = null;


        if (properties.length > 2) {
            address = properties[2];
        }
        if (properties.length > 1) {
            transactionId = properties[1];
        }
        // Unregister the event if already exists.
        if (this.transactionReceivers.containsKey(transactionId)) {
            TransactionReceiver receiver = this.transactionReceivers.get(transactionId);
            receiver.unregister();
            this.transactionReceivers.remove(transactionId);
        }

        Log.v("Bluetooth", "Received event " + eventName + " " + transactionId);
        TransactionReceiver receiver = null;

        switch (eventName) {

            case "rnbluetoothle.onStateChange":

                receiver = new AdapterStateChangeReceiver(this.reactContext, transactionId);
                receiver.register();
                this.transactionReceivers.put(transactionId, receiver);
                break;

            case "rnbluetoothle.onDiscovery":
                receiver = new DiscoveryReceiver(this.reactContext, transactionId);
                receiver.register();
                this.transactionReceivers.put(transactionId, receiver);
                break;

            case "rnbluetoothle.onBondedDevices":
                receiver = new BondsReceiver(this.reactContext, transactionId);
                receiver.register();
                this.transactionReceivers.put(transactionId, receiver);
                break;

            case "rnbluetoothle.onBondChange":
                receiver = new BondReceiver(this.reactContext, transactionId, address);
                receiver.register();
                this.transactionReceivers.put(transactionId, receiver);
                break;

            case "rnbluetoothle.onChange/":
                receiver = new ChangeReceiver(this.reactContext, transactionId, address);
                receiver.register();
                this.transactionReceivers.put(transactionId, receiver);
                break;

            case "rnbluetoothle.onReadCharacteristic/":
                //receiver = new ChangeReceiver(this.reactContext, transactionId);
                break;
            default:
                break;
        }

    }

    /**
     * Remove a JS module event listener.
     */
    @Override
    public void removeListener(String eventName) {
        String[] properties = this.getEventProperties(eventName);
        eventName = properties[0];
        if (properties.length > 1) {
            String transactionId = properties[1];

            if (this.transactionReceivers.containsKey(transactionId)) {
                TransactionReceiver receiver = this.transactionReceivers.get(transactionId);
                receiver.unregister();
                this.transactionReceivers.remove(transactionId);
            }
        }
    }

    /**
     * Enable the characteristic value notification.
     * If can't returns false.
     *
     * @param transactionId
     * @param address
     * @param serviceUUID
     * @param characteristicUUID
     * @return
     */
    @Override
    public boolean enableNotification(
            String address,
            String serviceUUID,
            String characteristicUUID,
            String transactionId) {
        JsBluetoothDeviceGattCallback gattCallback = this.getGattConnectionCallback(address);
        if (gattCallback == null) {
            return false;
        }

        BluetoothGatt gatt = this.getGattConnection(address);
        return gattCallback.addCharacteristicNotification(gatt, serviceUUID, characteristicUUID, transactionId);
    }

    /**
     * Disable the characteristic value notification.
     * If can't returns false.
     *
     * @param transactionId
     * @param address
     * @param serviceUUID
     * @param characteristicUUID
     * @return
     */
    @Override
    public boolean disableNotification(
            String address,
            String transactionId) {
        JsBluetoothDeviceGattCallback gattCallback = this.getGattConnectionCallback(address);
        if (gattCallback == null) {
            return false;
        }

        BluetoothGatt gatt = this.getGattConnection(address);
        return gattCallback.removeCharacteristicNotification(gatt, transactionId);
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
                Log.v("Bluetooth",device.getAddress()+" "+ id);
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
    public void connect(String id, Promise promise) {
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
    @Override
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

    /**
     * Write characteristic value.
     *
     * @param id
     * @param serviceUUID
     * @param characteristicUUID
     * @param value
     * @return
     */
    @Override
    public boolean writeValue(String id, String serviceUUID, String characteristicUUID, ReadableArray value) {
        JsBluetoothDeviceGattCallback jsBluetoothDeviceGattCallback = this.getGattConnectionCallback(id);
        BluetoothGatt gatt = this.getGattConnection(id);
        if (jsBluetoothDeviceGattCallback != null && gatt != null) {
            jsBluetoothDeviceGattCallback.writeCharacteristic(gatt, serviceUUID, characteristicUUID, value);
        }
        return false;
    }

    /**
     * Gets the event properties such: transactionId, eventName and address from
     * event name.
     * The event name can be:
     * - anyeventname/transactionid
     * - anyeventname/transactionid/deviceId
     *
     * @return
     */
    private String[] getEventProperties(String eventName) {
        String[] properties = eventName.split(Pattern.quote("/"));
        Log.v("Bluetooth", Arrays.toString(properties));

        return properties;
    }

    /**
     * Gets the JsBluetoothDeviceGattCallback of remote device by its address.
     * If the GATT connection is not stablished with the remote device it creates a
     * brand new Gatt connection and if all goes well it returns a BluetoothGatt.
     */
    private BluetoothGatt getOrSetAndStablishGattConnection(String address) {
        BluetoothGatt gatt = this.getGattConnection(address);
        if (gatt == null) {
            JsBluetoothDeviceGattCallback gattCallback = new JsBluetoothDeviceGattCallback(this.reactContext, address);
            BluetoothDevice device = BluetoothState.getRemoteDevice(address, this.reactContext);
            gatt = device.connectGatt(this.reactContext, false, gattCallback);

            if (gatt == null && gatt.connect()) {
                return null;
            }
            this.deviceBluetoothGatts.put(address, gatt);
            this.deviceBluetoothGattCallbacks.put(address, gattCallback);
        }

        if(!this.getIsConnected(address)){
           if(!gatt.connect()){
               this.deviceBluetoothGatts.remove(address);
               this.deviceBluetoothGattCallbacks.remove(address);
               return null;
           }
        }
        return gatt;
    }

    /**
     * Gets the current remote device GATT connection.
     *
     * @return
     */
    private BluetoothGatt getGattConnection(String address) {
        BluetoothDevice device = BluetoothState.getRemoteDevice(address, this.reactContext);
        BluetoothGatt gatt = this.deviceBluetoothGatts.get(address);
        return gatt;
    }

    /**
     * Gets the current remote device GATT callback.
     *
     * @return
     */
    private JsBluetoothDeviceGattCallback getGattConnectionCallback(String address) {
        BluetoothDevice device = BluetoothState.getRemoteDevice(address, this.reactContext);
        JsBluetoothDeviceGattCallback gatt = this.deviceBluetoothGattCallbacks.get(address);
        return gatt;
    }

    /**
     * Disconnect and remove the current address gatt connection.
     *
     * @return
     */
    private void removeGattConnection(String address) {
        BluetoothGatt gatt = this.getGattConnection(address);
        if (gatt != null) {
            gatt.disconnect();
            this.deviceBluetoothGatts.remove(address);
            this.deviceBluetoothGattCallbacks.remove(address);
        }
    }
}
