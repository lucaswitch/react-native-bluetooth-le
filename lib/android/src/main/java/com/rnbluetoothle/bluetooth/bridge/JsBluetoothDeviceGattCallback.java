package com.rnbluetoothle.bluetooth.bridge;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothStatusCodes;
import android.os.Build;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;

import com.rnbluetoothle.bluetooth.GattHelper;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothConnectionState;
import com.rnbluetoothle.bluetooth.bridge.JsEventDispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Responsible to deal with BluetoothDevice GATT events.
 * Once the GATT is connected then it can send JS events.
 */
public class JsBluetoothDeviceGattCallback extends BluetoothGattCallback {

    final private List<Map<String, String>> notifications;

    final protected String EVENT_ON_NOTIFY = "rnbluetoothle.onMonitorValue/";
    final protected String EVENT_ON_CHANGE = "rnbluetoothle.onChange/";

    /**
     * React application context.
     */
    ReactApplicationContext reactContext;
    protected int prevConnectionState;
    protected String address;

    public JsBluetoothDeviceGattCallback(ReactApplicationContext context, String address) {
        super();
        this.address = address;
        this.reactContext = context;

        this.notifications = new ArrayList<>();
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

        JsBluetoothConnectionState jsBluetoothConnectionState =
                new JsBluetoothConnectionState(
                        newConnectionState,
                        this.prevConnectionState
                );
        JsEventDispatcher.send(
                this.reactContext,
                this.EVENT_ON_CHANGE,
                jsBluetoothConnectionState.getMap()
        );
    }

    /**
     * On device GATT services are discovered.
     * Sends event to JS as soon the services are discovered, send the nested
     * characteristics too.
     */
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        /*
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
         */
    }

    /**
     * On device rssi read.
     */
    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {

    }

    /**
     * On device characteristic value just changed.
     */
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] value) {

        BluetoothGattService service = characteristic.getService();
        for (Map<String, String> notification : this.notifications) {
            String serviceUUID = notification.get("serviceUUID");
            if (serviceUUID != null && serviceUUID.equals(service.getUuid().toString())) {
                String characteristicUUID = notification.get("characteristicUUID");
                if (characteristicUUID != null && characteristicUUID.equals(characteristic.getUuid().toString())) {
                    String event = this.EVENT_ON_NOTIFY + notification.get("transactionId");
                    JsEventDispatcher.send(this.reactContext, event, value);
                }
            }
        }
    }

    /* ----------------------- Imperative Methods ----------------------- */

    /**
     * Writes the value into characteristic.
     *
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     * @param value
     * @return
     */
    public boolean writeCharacteristic(BluetoothGatt gatt,
                                       String serviceUUID,
                                       String characteristicUUID,
                                       byte[] value) {
        BluetoothGattCharacteristic characteristic = GattHelper.getCharacteristic(gatt, serviceUUID, characteristicUUID);
        if (characteristic != null) {
            if (Build.VERSION.SDK_INT < 33) {
                characteristic.setValue(value);
                return gatt.writeCharacteristic(characteristic);
            } else {
                return BluetoothStatusCodes.SUCCESS == gatt.writeCharacteristic(characteristic, value, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            }
        }
        return false;
    }

    /**
     * Writes the value into characteristic.
     *
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     * @param value
     * @return
     */

    public boolean writeCharacteristic(BluetoothGatt gatt,
                                       String serviceUUID,
                                       String characteristicUUID,
                                       int[] value) {
        byte[] byteArr = new byte[value.length];
        for (int i = 0; i < byteArr.length; i++) {
            byteArr[i] = (byte) value[i];
        }
        return this.writeCharacteristic(gatt, serviceUUID, characteristicUUID, byteArr);
    }

    public boolean writeCharacteristic(BluetoothGatt gatt,
                                       String serviceUUID,
                                       String characteristicUUID,
                                       ReadableArray value) {
        int length = value.size();
        byte[] byteArr = new byte[length];
        for (int i = 0; i < length; i++) {
            byteArr[i] = (byte) value.getInt(i);
        }
        return this.writeCharacteristic(gatt, serviceUUID, characteristicUUID, byteArr);
    }

    /**
     * Enables notification for characteristic.
     *
     * @return True if successfully enabled notification for characteristic.
     */
    public boolean addCharacteristicNotification(
            BluetoothGatt gatt,
            String serviceUUID,
            String characteristicUUID,
            String transactionId) {

        BluetoothGattCharacteristic characteristic =
                GattHelper.getCharacteristic(
                        gatt,
                        serviceUUID,
                        characteristicUUID
                );

        if (characteristic != null) {
            boolean isNotifying = (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
            Map<String, String> event = new HashMap<>();
            event.put("characteristicUUID", characteristicUUID);
            event.put("serviceUUID", serviceUUID);
            event.put("transactionID", transactionId);

            if (isNotifying) {
                this.notifications.add(event);
                return true;
            } else if (gatt.setCharacteristicNotification(characteristic, true)) {
                this.notifications.add(event);
                return true;
            }
        }

        return false;
    }

    /**
     * Disables notification for characteristic.
     * If the transaction does not exists or it's cancelled safely returns true.
     */
    public boolean removeCharacteristicNotification(
            BluetoothGatt gatt,
            String transactionId
    ) {
        for (Map<String, String> notification : this.notifications) {
            String notificationTransactionId = notification.get("transactionId");
            if (notificationTransactionId != null && notificationTransactionId.equals(transactionId)) {

                String serviceUUID = notification.get("serviceUUID");
                String characteristicUUID = notification.get("characteristicUUID");
                BluetoothGattCharacteristic characteristic = GattHelper.getCharacteristic(
                        gatt,
                        serviceUUID,
                        characteristicUUID);

                if (characteristic != null) {
                    boolean isNotifying =
                            (characteristic.getProperties()
                                    & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;

                    if (isNotifying) {
                        if (!gatt.setCharacteristicNotification(characteristic, false)) {
                            return false; // Could not cancel de notification.
                        }
                    }

                    Iterator<Map<String, String>> iterator = this.notifications.iterator();
                    while (iterator.hasNext()) {
                        Map<String, String> map = iterator.next();
                        String currentTransactionId = map.get("transactionId");
                        if (currentTransactionId == null || currentTransactionId.equals(transactionId)) {
                            iterator.remove();
                            break;
                        }
                    }

                    return true;
                }
                return true;
            }
        }
        return true;
    }
}
