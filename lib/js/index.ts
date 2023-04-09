import BluetoothModule from './NativeReactNativeBluetoothLe';
import {StatusChange} from "./types";

export const Bluetooth = {

    /**
     * On bluetooth peripheral has state change such as "on" and "off".
     * @param callback
     */
    subscribeStateChange(callback: (data: StatusChange) => void): Function {
        BluetoothModule.subscribeStateChange(callback);
        return function () {
            BluetoothModule.unsubscribeStateChange();
        }
    }
}