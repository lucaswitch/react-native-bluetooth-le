import BluetoothModule from './NativeReactNativeBluetoothLe';
import {
    BluetoothDevice,
    AdapterStatusEvent,
    BluetoothConnectionPriority,
    BluetoothConnectionStatusEvent,
    BluetoothDeviceCharacteristic,
    BluetoothDeviceCharacteristicValue
} from "./types";
import {NativeEventEmitter, NativeModules, Platform, PermissionsAndroid} from 'react-native';
// @ts-ignore
import {BondError} from "./exceptions";

// @ts-ignore
// @ts-ignore
// @ts-ignore
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
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }
        return BluetoothModule.getIsEnabled();
    },

    /**
     * Gets whether location is enabled for the application.
     *
     * On Android:
     *  Some bluetooth operations in Android can expose the user location, so to work you must check if user consents its location.
     *  Make sure that the permission "android.permission.ACCESS_FINE_LOCATION" is granted.
     */
    getIsLocationEnabled(): boolean {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }
        return BluetoothModule.getIsLocationEnabled();
    },

    /**
     * Gets system bluetooth adapter name.
     * On android:
     *   It maybe returns a empty string if the application could not find a system supported bluetooth adapter for low energy.
     *   To avoid it call "getIsSupported" to verify if has indeed a bluetooth adapter before calling this method.
     */
    getName(): string {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }
        return BluetoothModule.getAdapterName();
    },

    /**
     * Gets system bluetooth adapter address.
     * On android:
     *   It maybe returns a empty string if the application could not find a system supported bluetooth adapter for low energy.
     *   To avoid it call "getIsSupported" to verify if has indeed a bluetooth adapter before calling this method.
     */
    getAddress(): string {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }
        return BluetoothModule.getAdapterAddress();
    },

    /**
     * Gets whether device has a bluetooth adapter and supports bluetooth low energy.
     * On Android:
     *  Make sure to ask the android.bluetooth needed permissions.
     */
    getIsSupported(): boolean {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }
        return BluetoothModule.getIsSupported();
    },

    /**
     * Gets whether de following device id is bonding.
     * On Android:
     *  The id is the remote device mac address.
     *  Make sure to ask the user the following permissions or your application:
     *     - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *     - android.permission.BLUETOOTH_ADMIN in your AndroidManifest.xml file.
     *     - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     *  @param id
     */
    getIsBonded(id: string): boolean {
        if (!id || id.length === 0) {
            throw new Error('"id" must be a valid platform address for targeting bluetooth device.');
        }

        // Check platform specific permissions.
        // @ts-ignore
        if (__DEV__) {
            // Android specific permissions
            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    PermissionsAndroid.check(permission)
                        .then((granted) => {
                            if (!granted) {
                                console.error(`Before calling "getIsBonded" function make sure to ask the user "${permission}" permission or it won't work or either return always "false" or break/crash the application.`);
                            }
                        });
                }
            }
        }

        return BluetoothModule.getIsBonded(id);
    },

    /**
     * Tries to create a bond with the device, returns false if is not possible.
     * Returns true if the bond has successfully started, not if is already is bonding.
     * Note: To listen bonding state use "onBondChange" method.

     * On Android:
     *  The id is the remote device mac address.
     *  Make sure to ask the user the following permissions or your application:
     *      - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *      - android.permission.BLUETOOTH_ADMIN in your AndroidManifest.xml file.
     *      - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     *  @param id
     * @throws Error if can't bond with the device.
     */
    // @ts-ignore
    async bond(id: string): Promise<undefined> {
        if (!id || id.length === 0) {
            throw new Error('"id" must be a valid platform address for targeting bluetooth device.');
        }

        // Check platform specific permissions.
        // @ts-ignore
        if (__DEV__) {
            // Android specific permissions
            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    const granted = await PermissionsAndroid.check(permission);
                    if (!granted) {
                        throw new Error(`Before calling "bond" function make sure to ask the user "${permission}" permission or it won't work or either return always "false" or break/crash the application.`)
                    }
                }
            }
        }

        const error = BluetoothModule.bond(id)
        if (error.length > 0) {
            throw new BondError(error);
        }
    },

    /**
     * Tries unbond device, returns false if is not possible.
     * Returns true if the unbond has successful.
     * If already unbounded it returns true.
     * Note: To listen bonding state use "onBondChange" method.
     * On Android:
     *    The id is the remote device mac address.
     *    Make sure to ask the user the following permissions or your application:
     *    - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_ADMIN in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     * @return boolean Whether the unbonding was successfully.
     * @throws Error if can't unbond with the device.
     */
    // @ts-ignore
    async unBond(id: string): Promise<boolean> {
        if (!id || id.length === 0) {
            throw new Error('"id" must be a valid platform address for targeting bluetooth device.');
        }

        // Check platform specific permissions.
        // @ts-ignore
        if (__DEV__) {
            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    const granted = await PermissionsAndroid.check(permission);
                    if (!granted) {
                        throw new Error(`Before "unbound" function make sure to ask the user "${permission}" permission or it won't work, return always "false" or break the application.`)
                    }
                }
            }
        }

        const error = BluetoothModule.unBond(id)
        if (error.length > 0) {
            throw new BondError(error);
        }
    },

    /**
     * Gets the current bonding devices.
     * On Android:
     *    The id is the remote device mac address.
     *    Make sure to ask the user the following permissions or your application:
     *    - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_ADMIN in your AndroidManifest.xml file.
     *    - android.permission.ACCESS_FINE_LOCATION in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     * @return boolean Whether the unbonding was successfully.
     */
    getBondedDevices(): BluetoothDevice[] {
        return BluetoothModule.getBondedDevices();
    },

    /**
     * On bluetooth peripheral has state change such as "on" and "off".
     * The callback function is triggered always when the bluetooth power status has changed.
     * @param callback
     */
    onStateChange(callback: (data: AdapterStatusEvent) => void): Function {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }
        if (typeof callback !== 'function') {
            throw new Error("The callback of onStateChange must be a function that handles bluetooth state change.");
        }


        // Trigger on load first time.
        if (this.getIsEnabled()) {
            callback({status: 'on'});
        } else {
            callback({status: 'off'});
        }
        const event = 'rnbluetoothle.onStateChange/' + generateUUID();
        const bluetoothEventEmitter: NativeEventEmitter = new NativeEventEmitter(NativeModules.ReactNativeBluetoothLe);

        BluetoothModule.addListener(event);
        const eventListener = bluetoothEventEmitter.addListener(event, callback);
        return function () {
            BluetoothModule.removeListener(event);
            eventListener.remove();
        }
    },

    /**
     * Listen for bluetooth low energy nearby devices through the procedure called "discovery".
     * The devices will not be duplicated, the device is distinguished by "address" attributes of devices.
     * @param callback
     */
    onDiscovery(callback: (devices: BluetoothDevice[]) => void): Function {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }
        if (typeof callback !== 'function') {
            throw new Error("The callback of onDiscover must be a function that handles bluetooth discovery.");
        }

        const event = `rnbluetoothle.onDiscovery.${generateUUID()}`;
        const nativeEventEmitter: NativeEventEmitter = new NativeEventEmitter(NativeModules.ReactNativeBluetoothLe);
        const devices: BluetoothDevice[] = [];
        const eventListener = nativeEventEmitter.addListener(event, (device: BluetoothDevice) => {
            if (device.address === null) {
                return;
            }
            for (const i in devices) {
                if (devices[i].address == device.address) {
                    devices[i] = {...devices[i], ...device};
                    devices.sort((a, b) => b.rssi - a.rssi);
                    callback(devices);
                    return;
                }
            }

            devices.push(device);
            devices.sort((a, b) => b.rssi - a.rssi);
            callback(devices);
        });
        BluetoothModule.addListener(event);
        return function () {
            BluetoothModule.removeListener(event);
            eventListener.remove();
        }
    },

    /**
     * Gets the bonded devices everytime they are bonded.
     * On Android:
     *    The id is the remote device mac address.
     *    Make sure to ask the user the following permissions or your application:
     *    - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_ADMIN in your AndroidManifest.xml file.
     *    - android.permission.ACCESS_FINE_LOCATION in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     * @return boolean Whether the unbonding was successfully.
     */
    onBondedDevices(callback: (devices: BluetoothDevice[]) => void) {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }

        // @ts-ignore
        if (__DEV__) {
            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    PermissionsAndroid.check(permission)
                        .then((granted) => {
                            if (!granted) {
                                console.error(`Before "onBondChange" function make sure to ask the user "${permission}" permission or it won't work, return always "false" or break the application.`);
                            }
                        });
                }
            }
        }

        const event = 'rnbluetoothle.onBondedDevices.' + generateUUID();
        const bluetoothEventEmitter: NativeEventEmitter = new NativeEventEmitter(NativeModules.ReactNativeBluetoothLe);

        BluetoothModule.addListener(event);
        const eventListener = bluetoothEventEmitter.addListener(event, callback);

        return function () {
            eventListener.remove();
            BluetoothModule.removeListener(event);
        }
    },

    /**
     * Gets whether device bond state has changed.
     * On Android:
     *    The id is the remote device mac address.
     *    Make sure to ask the user the following permissions or your application:
     *    - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_ADMIN in your AndroidManifest.xml file.
     *    - android.permission.ACCESS_FINE_LOCATION in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     * @return boolean Whether the unbonding was successfully.
     */
    onBondChange(id: string, callback: (device: BluetoothDevice) => void): Function {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }

        // @ts-ignore
        if (__DEV__) {
            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    PermissionsAndroid.check(permission)
                        .then((granted) => {
                            if (!granted) {
                                console.error(`Before "onBondChange" function make sure to ask the user "${permission}" permission or it won't work, return always "false" or break the application.`);
                            }
                        });
                }
            }
        }

        const event = `rnbluetoothle.onBondChange.${generateUUID()}`;
        const bluetoothEventEmitter: NativeEventEmitter = new NativeEventEmitter(NativeModules.ReactNativeBluetoothLe);
        BluetoothModule.addListener(event);
        const eventListener = bluetoothEventEmitter.addListener(event, callback);
        return function () {
            BluetoothModule.removeListener(event);
            eventListener.remove();
        }
    },

    /**
     * Gets whether device attributes has changed.
     * On Android:
     *    The id is the remote device mac address.
     *    Make sure to ask the user the following permissions or your application:
     *    - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_ADMIN in your AndroidManifest.xml file.
     *    - android.permission.ACCESS_FINE_LOCATION in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     * @return boolean Whether the unbonding was successfully.
     */
    onChange(id: string, callback: (device: BluetoothDevice) => void): Function {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }

        // @ts-ignore
        if (__DEV__) {
            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    PermissionsAndroid.check(permission)
                        .then((granted) => {
                            if (!granted) {
                                console.error(`Before "onChange" function make sure to ask the user "${permission}" permission or it won't work, return always "false" or break the application.`);
                            }
                        });
                }
            }
        }

        const event = `rnbluetoothle.onChange.${generateUUID()}`;
        const bluetoothEventEmitter: NativeEventEmitter = new NativeEventEmitter(NativeModules.ReactNativeBluetoothLe);
        BluetoothModule.addListener(event);
        const eventListener = bluetoothEventEmitter.addListener(event, callback);
        return function () {
            BluetoothModule.removeListener(event);
            eventListener.remove();
        }
    },

    /**
     * Gets if device is connected.
     * On Android:
     *    The id is the remote device mac address.
     *     Make sure to ask the user the following permissions or your application or this won't work just as expected:
     *    - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *    - android.permission.ACCESS_FINE_LOCATION in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     */
    getIsConnected(id: string): boolean {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }

        // Check platform specific permissions.
        // @ts-ignore
        if (__DEV__) {
            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    const granted = PermissionsAndroid.check(permission)
                        .then((granted) => {
                            if (!granted) {
                                throw new Error(`Before "getIsConnected" function make sure to ask the user "${permission}" permission or it won't work, return always "false" or break the application.`)
                            }
                        });
                }
            }
        }

        return BluetoothModule.getIsConnected(id)
    },

    /**
     * Tries to establish a GATT connection with remote device.
     * On Android:
     *    The id is the remote device mac address.
     *    Make sure to ask the user the following permissions or your application or this won't work just as expected:
     *    - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *    - android.permission.ACCESS_FINE_LOCATION in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     */
    async connect(id: string, priority?: BluetoothConnectionPriority) {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }
        if (!id || id.length === 0) {
            throw new Error('"id" must be a valid platform address for targeting bluetooth device.');
        }
        // Check platform specific permissions.
        // @ts-ignore
        if (__DEV__) {
            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    const granted = await PermissionsAndroid.check(permission);
                    if (!granted) {
                        throw new Error(`Before "connect" function make sure to ask the user "${permission}" permission or it won't work, return always "false" or break the application.`)
                    }
                }
            }
        }

        return await BluetoothModule.connect(id);
    },

    /**
     * Tries to disconnect GATT connection with remote device.
     * On Android:
     *    The id is the remote device mac address.
     *    Make sure to ask the user the following permissions or your application or this won't work just as expected:
     *    - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *    - android.permission.ACCESS_FINE_LOCATION in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     */
    async disconnect(id: string): Promise<void> {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }
        if (!id || id.length === 0) {
            throw new Error('"id" must be a valid platform address for targeting bluetooth device.');
        }

        // Check platform specific permissions.
        // @ts-ignore
        if (__DEV__) {
            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    const granted = await PermissionsAndroid.check(permission);
                    if (!granted) {
                        throw new Error(`Before "disconnect" function make sure to ask the user "${permission}" permission or it won't work, return always "false" or break the application.`)
                    }
                }
            }
        }

        return await BluetoothModule.disconnect(id);
    },

    /**
     * Monitor the characteristic from remote device.
     * Make sure the remove device is connected calling the "getIsConnected" method.
     * On Android:
     *    The id is the remote device mac address.
     *    Make sure to ask the user the following permissions or your application or this won't work just as expected:
     *    - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *    - android.permission.ACCESS_FINE_LOCATION in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     */
    onMonitorValue(id: string, serviceUUID: string, characteristicUUID: string, callback: (value: BluetoothDeviceCharacteristicValue) => void): Function {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        }
        if (!id || id.length === 0) {
            throw new Error('"id" must be a valid platform address for targeting bluetooth device.');
        }

        // Check platform specific permissions.
        // @ts-ignore
        if (__DEV__) {
            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    PermissionsAndroid.check(permission)
                        .then((granted) => {
                            if (!granted) {
                                throw new Error(`Before "onMonitorValue" function make sure to ask the user "${permission}" permission or it won't work, return always "false" or break the application.`)
                            }
                        })
                }
            }
        }

        const transactionId = generateUUID();
        const event = `rnbluetoothle.onMonitorValue.${transactionId}`;

        // Setup JNI event listeners.
        const {ReactNativeBluetoothLe} = NativeModules;
        const bluetoothEventEmitter: NativeEventEmitter = new NativeEventEmitter(ReactNativeBluetoothLe);
        BluetoothModule.addListener(event);
        const eventListener = bluetoothEventEmitter.addListener(event, callback);

        // Enable bluetooth notification
        let enabled = BluetoothModule.enableNotification(id, serviceUUID, characteristicUUID, transactionId);
        if (!enabled) {
            throw new Error(`It was not possible to enable bluetooth notification for characteristic ${characteristicUUID}.`);
        }

        return function () {
            // Disable JNI events.
            BluetoothModule.removeListener(event);
            eventListener.remove();

            if (BluetoothModule.disableNotification(id, transactionId)) {
                throw new Error(`It was not possible to disable the bluetooth for characteristic ${characteristicUUID}.`)
            }
        }

    },

    /**
     * Write the characteristic value from remote device.
     * Make sure the remove device is connected calling the "getIsConnected" method.
     * On Android:
     *    The id is the remote device mac address.
     *    Make sure to ask the user the following permissions or your application or this won't work just as expected:
     *    - android.permission.BLUETOOTH in your AndroidManifest.xml file.
     *    - android.permission.ACCESS_FINE_LOCATION in your AndroidManifest.xml file.
     *    - android.permission.BLUETOOTH_CONNECT before using this function on application code.
     */
    async writeValue(id: string, serviceUUID: string, characteristicUUID: string, value: number[]): Promise<void> {
        if (!BluetoothModule) {
            throw new Error("It was not possible to find BluetoothModule.");
        } else if (!id || id.length === 0) {
            throw new Error('"id" must be a valid platform address for targeting bluetooth device.');
        } else if (value.length === 0) {
            return;
        }

        // Check platform specific permissions.
        // @ts-ignore
        if (__DEV__) {
            // Check the validity of values, must be js numbers that represents bytes.
            for (const i in value) {
                const isValid = value[i] >= 0 && value[i] <= 256;
                if (!isValid) {
                    throw new Error(`The value provided "${value[i]}" is not a valid number as characteristic writable value.`);
                }
            }

            if (Platform.OS === 'android') {
                const neededPermissions = ['android.permission.BLUETOOTH_CONNECT']
                for (const permission of neededPermissions) {
                    // @ts-ignore
                    const granted = await PermissionsAndroid.check(permission);
                    if (!granted) {
                        throw new Error(`Before "onMonitorValue" function make sure to ask the user "${permission}" permission or it won't work, return always "false" or break the application.`)
                    }
                }
            }
        }

        const transactionId = generateUUID();
        const event = `rnbluetoothle.onMonitorValue/${transactionId}`;
    }
}


function generateUUID() {
    return 'xxxxxxxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
            v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

export * from './types'
