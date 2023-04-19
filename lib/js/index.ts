import BluetoothModule from './NativeReactNativeBluetoothLe';
import {BluetoothDevice, StatusChange} from "./types";
// @ts-ignore
import {NativeEventEmitter, NativeModules, Platform} from 'react-native';

export * from './types'

/**
 * React Native Bluetooth Low Energy.
 * Licensed under MIT license definition.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”)...
 * Copyright 2023, @lucaswitch
 * More info: https://github.com/lucaswitch/react-native-bluetooth-le
 */
export const Bluetooth = {
    /**
     * Gets whether bluetooth default system adapter is enabled.
     * It returns always false if is enabled and does not support Bluetooth Low Energy.
     * Use "getIsSupported" method to check if the current running device has a Bluetooth Low Energy Adapter
     */
    getIsEnabled(): boolean {
        if (BluetoothModule) {
            return BluetoothModule.getIsEnabled();
        }
        return false;
    },

    /**
     * Gets whether location is enabled for the application.
     *
     * On Android:
     *  Some bluetooth operations in Android can expose the user location, so to work you must check if user consents its location.
     *  Make sure that the permission "android.permission.ACCESS_FINE_LOCATION" is granted.
     */
    getIsLocationEnabled(): boolean {
        if (BluetoothModule) {
            return BluetoothModule.getIsLocationEnabled();
        }
        return false;
    },

    /**
     * Gets system bluetooth adapter name.
     * On android:
     *   It maybe returns a empty string if the application could not find a system supported bluetooth adapter for low energy.
     *   To avoid it call "getIsSupported" to verify if has indeed a bluetooth adapter before calling this method.
     */
    getName(): string {
        if (BluetoothModule) {
            return BluetoothModule.getAdapterName();
        }
        return "";
    },

    /**
     * Gets system bluetooth adapter address.
     * On android:
     *   It maybe returns a empty string if the application could not find a system supported bluetooth adapter for low energy.
     *   To avoid it call "getIsSupported" to verify if has indeed a bluetooth adapter before calling this method.
     */
    getAddress(): string {
        if (BluetoothModule) {
            return BluetoothModule.getAdapterAddress();
        }
        return "";
    },

    /**
     * Gets whether device has a bluetooth adapter and supports bluetooth low energy.
     * On Android:
     *  Make sure to ask the android.bluetooth needed permissions.
     */
    getIsSupported(): boolean {
        if (BluetoothModule) {
            return BluetoothModule.getIsSupported();
        }
        return false;
    },

    /**
     * On bluetooth peripheral has state change such as "on" and "off".
     * The callback function is triggered always when the bluetooth power status has changed.
     * @param callback
     */
    onStateChange(callback: (data: StatusChange) => void): Function {
        if (typeof callback !== 'function') {
            throw new Error("The callback of onStateChange must be a function that handles bluetooth state change.");
        }
        let eListener = null;
        let bluetoothEventEmitter: NativeEventEmitter | null = null;
        const eventName = 'rnbluetoothle.onStateChange';

        if (BluetoothModule) {
            bluetoothEventEmitter = new NativeEventEmitter(NativeModules.ReactNativeBluetoothLe);
            BluetoothModule.addListener(eventName);
            eListener = bluetoothEventEmitter.addListener(eventName, callback)
            // Trigger on load first time.
            if (this.getIsEnabled()) {
                callback({status: 'on'})
            } else {
                callback({status: 'off'})
            }
        }
        return function () {
            if (BluetoothModule) {
                BluetoothModule.removeListener(eventName);
            }
            if (eListener) {
                eListener.remove();
            }
        }
    },
    /**
     * Listen for bluetooth low energy nearby devices through the procedure called "discovery".
     * @param callback
     */
    onDiscovery(callback: (devices: BluetoothDevice[]) => void): Function {
        if (typeof callback !== 'function') {
            throw new Error("The callback of onDiscover must be a function that handles bluetooth discovery.");
        }

        let bluetoothEventEmitter: NativeEventEmitter | null = null;
        const eventName = 'rnbluetoothle.onDiscovery';
        const devices: BluetoothDevice[] = [];
        let eListener = null;

        if (BluetoothModule) {
            bluetoothEventEmitter = new NativeEventEmitter(NativeModules.ReactNativeBluetoothLe);
            eListener = bluetoothEventEmitter.addListener(eventName, (device: BluetoothDevice) => {
                if (device.address === null) {
                    console.log('device is null', device.address)
                    return;
                }
                for (const i in devices) {
                    if (devices[i].address == device.address) {
                        devices[i] = {...devices[i], ...device};
                        devices.sort((a, b) => b.rssi - a.rssi);
                        console.log('devices', devices)
                        callback(devices);
                        return;
                    }
                }

                devices.push(device);
                devices.sort((a, b) => b.rssi - a.rssi);
                callback(devices);
            });
            BluetoothModule.addListener(eventName);
        }
        return function () {
            if (BluetoothModule) {
                BluetoothModule.removeListener(eventName);
            }
            if (eListener) {
                eListener.remove();
            }
        }
    }
}




