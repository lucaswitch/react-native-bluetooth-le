package com.rnbluetoothle.bluetooth.bridge;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import com.rnbluetoothle.bluetooth.bridge.errors.JsDiscoveryError;
import com.rnbluetoothle.bluetooth.bridge.JsEventDispatcher;

import java.util.List;

/**
 * Responsible to deal with BluetoothDevice GATT events.
 * Once the GATT is connected then it can send JS events.
 */
public class JsDeviceGattCallback extends BluetoothGattCallback {
    protected String EVENT_ON_CONNECTION_CHANGE = "rnbluetoothle.onConnectionChange/";
    protected String EVENT_ON_DISCOVER_SERVICES = "rnbluetoothle.onDiscoverServices/";
    protected String EVENT_ON_RSSI = "rnbluetoothle.onRssi/";
    protected String EVENT_ON_CHARACTERISTIC_READ = "rnbluetoothle.onCharacteristicRead/";
    protected String EVENT_ON_ERROR = "rnbluetoothle.onError/";

    /**
     * React application context.
     */
    ReactApplicationContext reactContext;

    protected int prevConnectionState;
    protected String transactionId;

    public JsDeviceGattCallback(ReactApplicationContext context, String transactionId) {
        super();
        this.transactionId = transactionId;
        this.reactContext = context;

        this.EVENT_ON_CONNECTION_CHANGE += transactionId;
        this.EVENT_ON_DISCOVER_SERVICES += transactionId;
        this.EVENT_ON_RSSI += transactionId;
        this.EVENT_ON_CHARACTERISTIC_READ += transactionId;
        this.EVENT_ON_ERROR += transactionId;
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
                this.prevConnectionState
        );
        JsEventDispatcher.send(
                this.reactContext,
                this.EVENT_ON_CONNECTION_CHANGE,
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
                JsDiscoveryError jsDiscoveryError = new JsDiscoveryError(
                        JsDiscoveryError.ErrorCodes.GATT_FAILURE,
                        "Discovery just failed."
                );
                JsEventDispatcher.send(
                        this.reactContext,
                        this.EVENT_ON_ERROR,
                        jsDiscoveryError.getMap()
                );

                break;

            case BluetoothGatt.GATT_CONNECTION_CONGESTED:
                JsDiscoveryError jsDiscoveryError = new JsDiscoveryError(
                        JsDiscoveryError.ErrorCodes.GATT_CONNECTION_CONGESTED,
                        "The connection was congested, the discovery could not be finished."
                );
                JsEventDispatcher.send(
                        this.reactContext,
                        this.EVENT_ON_ERROR,
                        jsDiscoveryError.getMap()
                );
                break;

            case BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION:
                JsDiscoveryError jsDiscoveryError = new JsDiscoveryError(
                        JsDiscoveryError.ErrorCodes.GATT_INSUFFICIENT_AUTHENTICATION,
                        "Insufficient authentication, the discovery could not be finished."
                );
                JsEventDispatcher.send(
                        this.reactContext,
                        this.EVENT_ON_ERROR,
                        jsDiscoveryError.getMap()
                );
                break;

            case BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION:
                JsDiscoveryError jsDiscoveryError = new JsDiscoveryError(
                        JsDiscoveryError.ErrorCodes.GATT_INSUFFICIENT_ENCRYPTION,
                        "Insufficient encryption, the discovery could not be finished."
                );
                JsEventDispatcher.send(
                        this.reactContext,
                        this.EVENT_ON_ERROR,
                        jsDiscoveryError.getMap()
                );
                break;

            case BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED:
                JsDiscoveryError jsDiscoveryError = new JsDiscoveryError(
                        JsDiscoveryError.ErrorCodes.GATT_REQUEST_NOT_SUPPORTED,
                        "Request not supported, the discovery could not be finished."
                );
                JsEventDispatcher.send(
                        this.reactContext,
                        this.EVENT_ON_ERROR,
                        jsDiscoveryError.getMap()
                );
                break;

            default:
                break;
        }
    }]

    /**
     * On device rssi read.
     */
    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        if (BluetoothGatt.GATT_SUCCESS) {
            JsEventDispatcher.send(this.reactContext, this.EVENT_ON_RSSI, rssi);
        } else {
            JsEventDispatcher.send(this.reactContext, this.EVENT_ON_RSSI, null);
        }
    }

    /**
     * On device characteristic value read.
     */
    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] value, int status) {
        JsEventDispatcher.send(this.reactContext, this.EVENT_ON_CHARACTERISTIC_READ, value);
    }
}
