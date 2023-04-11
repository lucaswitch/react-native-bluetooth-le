import BluetoothModule from './NativeReactNativeBluetoothLe';
import {StatusChange} from "./types";
import{DeviceEventEmitter} from "react-native";

/**
 * Bluetooth Api.
 */
export const Bluetooth = {
    /**
     * On bluetooth peripheral has state change such as "on" and "off".
     * @param callback
     */
    onStateChange(callback: (data: StatusChange) => void): Function {
        BluetoothModule.enableStateChange();
        DeviceEventEmitter.addListener('onStateChange', callback)
        return function () {
            DeviceEventEmitter.removeAllListeners('onStateChange')
            BluetoothModule.disableStateChange();
        }
    }
}




