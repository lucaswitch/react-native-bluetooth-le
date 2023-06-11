package com.rnbluetoothle.bluetooth.bridge;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import com.rnbluetoothle.bluetooth.bridge.JsBluetoothDeviceServiceCharacteristic;

/**
 * Represents a Js single Bluetooth Remote Descriptor.
 */
public class JsBluetoothDeviceDescriptor {

    /**
     * Get the js descriptor properties.
     */
    public static WritableMap getMap(BluetoothGattDescriptor descriptor) {
        WritableMap jsCharacteristicDescriptor = Arguments.createMap();
        jsCharacteristicDescriptor.putString("id", descriptor.getUuid().toString());

        // Permissions
        WritableArray jsCharacteristicDescriptorPermissions = Arguments.createArray();
        for (String permission : JsBluetoothDeviceServiceCharacteristic.getPermissions(descriptor)){
            jsCharacteristicDescriptorPermissions.pushString(permission);
        }
        jsCharacteristicDescriptor.putArray("permissions", jsCharacteristicDescriptorPermissions);
        return jsCharacteristicDescriptor;
    }
}
