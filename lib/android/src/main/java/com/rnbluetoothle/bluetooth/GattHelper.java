package com.rnbluetoothle.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

public class GattHelper {
    /**
     * Gets the gatt characteristic in a imperative way.
     * 
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     */
    public static BluetoothGattCharacteristic getCharacteristic(BluetoothGatt gatt, UUID serviceUUID,
            UUID characteristicUUID) {
        BluetoothGattService service = GattHelper.getService(gatt, serviceUUID);
        if (service != null) {
            return service.getCharacteristic(characteristicUUID);
        }

        return null;
    }

    /**
     * Gets the gatt characteristic in a imperative way.
     * Just a shortcut method to convert from String to UUID.
     * 
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     */
    public static BluetoothGattCharacteristic getCharacteristic(
            BluetoothGatt gatt,
            String serviceUUID,
            String characteristicUUID) {
        return GattHelper.getCharacteristic(gatt, UUID.fromString(serviceUUID), UUID.fromString(characteristicUUID));
    }

    /**
     * Gets the gatt service in a imperative way.
     * 
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     */
    public static BluetoothGattService getService(BluetoothGatt gatt, UUID serviceUUID) {
        for (BluetoothGattService service : gatt.getServices()) {
            if (service.getUuid().equals(serviceUUID)) {
                return service;
            }
        }

        return null;
    }

    /**
     * Gets the gatt service in a imperative way.
     * Just a shortcut method to convert from String to UUID
     * 
     * 
     * @param gatt
     * @param serviceUUID
     * @param characteristicUUID
     */
    public static BluetoothGattService getService(BluetoothGatt gatt, String serviceUUID) {
        return GattHelper.getService(gatt, UUID.fromString(serviceUUID));
    }

    /**
     * Get all gatt services in a imperative way.
     * 
     */
    public static List<BluetoothGattService> getAllServices(BluetoothGatt gatt) {
        return gatt.getServices();
    }
}
