package com.rnbluetoothle.bluetooth.bridge;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;
import android.content.Intent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Promise;

import com.rnbluetoothle.bluetooth.GattHelper;
import com.rnbluetoothle.bluetooth.bridge.errors.JsDiscoveryError;
import com.rnbluetoothle.bluetooth.bridge.JsEventDispatcher;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothConnectionState;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDeviceService;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDeviceServiceCharacteristic;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Responsible to deal with BluetoothDevice GATT events.
 * Once the GATT is connected then it can send JS events.
 */
public class JsDeviceGattCallback extends BluetoothGattCallback {

    private List<Map<String, String>> notifications;
    protected String EVENT_PREFIX_ON_NOTIFY = "rnbluetoothle.onMonitorValue/";

    /**
     * React application context.
     */
    ReactApplicationContext reactContext;
    protected int prevConnectionState;
    protected String address;

    public JsDeviceGattCallback(ReactApplicationContext context, String address) {
        super();
        this.address = address;
        this.reactContext = context;

        this.notifications = new ArrayList();
    }

    /**
     * On device GATT connection state change.
     * Starts the discover as soon the device GATT connection is stablished.
     */
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newConnectionState) {
        BluetoothDevice device = gatt.getDevice();
        String address = device.getAddress();

        if (newConnectionState == BluetoothProfile.STATE_CONNECTED) {
            Log.v("Bluetooth", "Just connected on remote device " + address);
            gatt.discoverServices(); // Automatically discover remote device services when connected.
        }

        JsBluetoothConnectionState jsBluetoothConnectionState = new JsBluetoothConnectionState(
                newConnectionState,
                this.prevConnectionState);
        JsEventDispatcher.send(
                this.reactContext,
                this.EVENT_ON_CONNECTION_CHANGE,
                jsBluetoothConnectionState.getMap());
    }

    /**
     * On device GATT services are discovered.
     * Sends event to JS as soon the services are discovered, send the nested
     * characteristics too.
     */
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {

        JsDiscoveryError jsDiscoveryError;
        switch (status) {
            case BluetoothGatt.GATT_SUCCESS:
                List<BluetoothGattService> services = gatt.getServices();
                WritableArray jsServices = Arguments.createArray();
                for (BluetoothGattService service : services) {
                    jsServices.pushMap(JsBluetoothDeviceService.getMap(service));
                }

                JsEventDispatcher.send(this.reactContext, this.EVENT_ON_DISCOVER_SERVICES, jsServices);
                break;

            case BluetoothGatt.GATT_FAILURE:
                jsDiscoveryError = new JsDiscoveryError(
                        JsDiscoveryError.ErrorCodes.GATT_FAILURE,
                        "Discovery just failed.");
                JsEventDispatcher.send(
                        this.reactContext,
                        this.EVENT_ON_ERROR,
                        jsDiscoveryError.getMap());

                break;

            case BluetoothGatt.GATT_CONNECTION_CONGESTED:
                jsDiscoveryError = new JsDiscoveryError(
                        JsDiscoveryError.ErrorCodes.GATT_CONNECTION_CONGESTED,
                        "The connection was congested, the discovery could not be finished.");
                JsEventDispatcher.send(
                        this.reactContext,
                        this.EVENT_ON_ERROR,
                        jsDiscoveryError.getMap());
                break;

            case BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION:
                jsDiscoveryError = new JsDiscoveryError(
                        JsDiscoveryError.ErrorCodes.GATT_INSUFFICIENT_AUTHENTICATION,
                        "Insufficient authentication, the discovery could not be finished.");
                JsEventDispatcher.send(
                        this.reactContext,
                        this.EVENT_ON_ERROR,
                        jsDiscoveryError.getMap());
                break;

            case BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION:
                jsDiscoveryError = new JsDiscoveryError(
                        JsDiscoveryError.ErrorCodes.GATT_INSUFFICIENT_ENCRYPTION,
                        "Insufficient encryption, the discovery could not be finished.");
                JsEventDispatcher.send(
                        this.reactContext,
                        this.EVENT_ON_ERROR,
                        jsDiscoveryError.getMap());
                break;

            case BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED:
                jsDiscoveryError = new JsDiscoveryError(
                        JsDiscoveryError.ErrorCodes.GATT_REQUEST_NOT_SUPPORTED,
                        "Request not supported, the discovery could not be finished.");
                JsEventDispatcher.send(
                        this.reactContext,
                        this.EVENT_ON_ERROR,
                        jsDiscoveryError.getMap());
                break;

            default:
                break;
        }
    }

    /**
     * On device rssi read.
     */
    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {

    }

    /**
     * On device characteristic value read.
     */
    @Override
    public void onCharacteristicRead(
            BluetoothGatt gatt,
            BluetoothGattCharacteristic characteristic,
            byte[] value,
            int status) {

        // Deal with characteristic read promises.
        if (BluetoothGatt.GATT_SUCCESS == status) {

            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                Promise promise = iterator.next();
                iterator.remove();
                promise.resolve(
                        JsBluetoothDeviceServiceCharacteristic.getMap(characteristic, value));
            }
        } else {

            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                Promise promise = iterator.next();
                iterator.remove();

                promise.reject(
                        500, "Unable to read characteristic.");
            }
        }
    }

    /**
     * On device characteristic value just changed.
     */
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] value) {

        BluetoothGattService service = characteristic.getService();
        for (Map<String, String> notification : this.notifications) {
            String serviceUUID = notification.get("serviceUUID");
            if (serviceUUID.equals(service.getUuid().toString())) {
                String characteristicUUID = notification.get("characteristicUUID");
                if (characteristicUUID.equals(characteristic.getUuid().toString())) {
                    String event = this.EVENT_PREFIX_ON_NOTIFY + notification.getKey();
                    JsEventDispatcher.send(this.reactContext, event, value);
                }
            }
        }
    }

    /* ----------------------- Imperative Methods ------------------------------- */

    /**
     * Enables notification for characteristic.
     * 
     * @param transactionId
     * @return True if successfully enabled notification for characteristic.
     */
    public boolean addCharacteristicNotification(
            BluetoothGatt gatt,
            String serviceUUID,
            String characteristicUUID,
            String transactionId) {

        BluetoothGattCharacteristic characteristic = GattHelper.getCharacteristic(
                gatt,
                serviceUUID,
                characteristicUUID);

        if (characteristic != null) {
            boolean isNotifying = (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
            Map event = new HashMap<String, String>();
            event.put("characteristicUUID", characteristicUUID);
            event.put("serviceUUID", serviceUUID);

            if (isNotifying) {
                this.notifications.add(transactionId, event);
                return true;
            }
            if (bluetoothGatt.setCharacteristicNotification(characteristic, true)) {
                this.notifications.add(transactionId, event);
                return true;
            }
        }

        return false;
    }

    /**
     * Disables notification for characteristic.
     * If the transaction does not exists or it's cancelled safely returns true.
     * 
     */
    public boolean removeCharacteristicNotification(
            BluetoothGatt gatt,
            String transactionId) {
        for (Map<String, String> notification : this.notifications) {
            if (notification.getKey().equals(transactionId)) {

                String serviceUUID = notification.get("serviceUUID");
                String characteristicUUID = notification.get("characteristicUUID");
                BluetoothGattCharacteristic characteristic = GattHelper.getCharacteristic(
                        gatt,
                        serviceUUID,
                        characteristicUUID);

                if (characteristic != null) {
                    boolean isNotifying = (characteristic.getProperties()
                            & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;

                    if (isNotifying) {
                        if (!bluetoothGatt.setCharacteristicNotification(characteristic, false)) {
                            return false; // Could not cancel de notification.
                        }
                    }
                    this.notifications.remove(transactionId);
                    return true;
                }
                return true;
            }
        }
        return true;
    }
}
