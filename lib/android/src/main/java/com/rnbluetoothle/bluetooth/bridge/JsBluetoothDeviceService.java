package com.rnbluetoothle.bluetooth.bridge;

import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDeviceServiceCharacteristic;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Js single Bluetooth Remote device.
 */
public class JsBluetoothDeviceService {

    /**
     * Get service type.
     */
    private static String getType(BluetoothGattService service) {
        switch (service.getType()) {
            case BluetoothGattService.SERVICE_TYPE_PRIMARY:
                return "primary";
            case BluetoothGattService.SERVICE_TYPE_SECONDARY:
                return "secondary";
            default:
                return "unknown";
        }
    }

    /**
     * Gets the map.
     */
    public static WritableMap getMap(BluetoothGattService service) {
        WritableMap jsService = Arguments.createMap();

        jsService.putString("id", service.getUuid().toString());
        jsService.putString("type", JsBluetoothDeviceService.getType(BluetoothGattService));

        // Characteristics
        WritableArray jsCharacteristics = Arguments.createArray();
        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
            jsCharacteristics.pushMap(JsBluetoothDeviceServiceCharacteristic.getMap(characteristic));
        }
        jsService.putArray("characteristics", jsCharacteristics);

        return jsService;
    }
}
