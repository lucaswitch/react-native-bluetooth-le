package com.rnbluetoothle.bluetooth.bridge;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDeviceDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Js single Bluetooth Remote device service characteristic.
 */
public class JsBluetoothDeviceServiceCharacteristic {
    /**
     * Gets a list with the characteristic permissions.
     * They can be these, including multiple at same time:
     * - read
     * - write
     * - notify
     * - broadcast
     * - write_no_response
     */
    private static List<String> getPermissions(BluetoothGattCharacteristic characteristic) {
        int properties = characteristic.getProperties();
        List<String> propertiesList = new ArrayList<>();
        if ((properties & BluetoothGattCharacteristic.PROPERTY_READ) != 0) {
            propertiesList.add("read");
        }
        if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE) != 0) {
            propertiesList.add("write");
        }
        if ((properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
            propertiesList.add("notify");
        }
        if ((properties & BluetoothGattCharacteristic.PROPERTY_BROADCAST) != 0) {
            propertiesList.add("broadcast");
        }
        if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) != 0) {
            propertiesList.add("write_no_response");
        }
        return propertiesList;
    }

    /**
     * Gets a list with descriptor permissions.
     * They can be these, including multiple at same time:
     * - read
     * - write
     */
    public static List<String> getPermissions(BluetoothGattDescriptor descriptor) {
        List<String> propertiesList = new ArrayList<>();

        int permissions = descriptor.getPermissions();
        if ((permissions & BluetoothGattDescriptor.PERMISSION_READ) != 0) {
            propertiesList.add("read");
        }
        if ((permissions & BluetoothGattDescriptor.PERMISSION_WRITE) != 0) {
            propertiesList.add("write");
        }
        return propertiesList;
    }

    /**
     * Gets the map.
     */
    public static WritableMap getMap(BluetoothGattCharacteristic characteristic) {
        WritableMap jsCharacteristic = Arguments.createMap();
        jsCharacteristic.putString("id", characteristic.getUuid().toString());
        jsCharacteristic.putNull("value");

        // Permissions
        WritableArray jsCharacteristicPermissions = Arguments.createArray();
        for (String permission : JsBluetoothDeviceServiceCharacteristic.getPermissions(characteristic)) {
            jsCharacteristicPermissions.pushString(permission);
        }
        jsCharacteristic.putArray("permissions", jsCharacteristicPermissions);

        // Descriptors
        WritableArray jsCharacteristicDescriptors = Arguments.createArray();
        for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
            jsCharacteristicDescriptors.pushMap(JsBluetoothDeviceDescriptor.getMap(descriptor));
        }

        jsCharacteristic.putArray("descriptors", jsCharacteristicDescriptors);

        return jsCharacteristic;
    }

    /**
     * Gets the map with value, descriptors and permissions are removed if so.
     */
    public static WritableMap getMap(BluetoothGattCharacteristic characteristic, byte[] value) {
        WritableMap jsCharacteristic = JsBluetoothDeviceServiceCharacteristic.getMap(characteristic);
        jsCharacteristic.putString("id", characteristic.getUuid().toString());
        WritableArray jsCharacteristicValue = Arguments.createArray();
        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                jsCharacteristicValue.pushInt(value[i] & 0xFF);
            }
        }
        jsCharacteristic.putArray("value", jsCharacteristicValue);
        return jsCharacteristic;
    }
}
