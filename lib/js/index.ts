import BluetoothModule from './NativeReactNativeBluetoothLe';
import {StatusChange} from "./types";
import {NativeEventEmitter, NativeModules} from 'react-native';

/**
 * Bluetooth Api.
 */
export const Bluetooth = {
    /**
     * On bluetooth peripheral has state change such as "on" and "off".
     * @param callback
     */
    onStateChange(callback: (data: StatusChange) => void): Function {
        if (typeof callback !== 'function') {
            throw new Error("The callback of onStateChange must be a function that handles bluetooth state change.");
        }
        const bluetoothEventEmitter = new NativeEventEmitter(NativeModules.ReactNativeBluetoothLe);
        const eventName = 'rnbluetoothle.onStateChange';
        BluetoothModule.addListener(eventName);
        bluetoothEventEmitter.addListener(eventName, callback)
        return function () {
            BluetoothModule.removeListener(eventName);
            bluetoothEventEmitter.removeListeners();
        }
    }
}




