import type {TurboModule} from 'react-native/Libraries/TurboModule/RCTExport';
import {TurboModuleRegistry} from 'react-native';
import {StatusChange} from "./types";

export interface Spec extends TurboModule {
    /**
     * Turn on Bluetooth Module of device if possible.
     */
    turnOnIfPossible(): void;

    /**
     * Turn off Bluetooth Module of device.
     */
    turnOffIfPossible(): void;

    /**
     * Gets whether bluetooth is supported.
     */
    getIsSupported(): boolean;

    /**
     * Subscribe for events such "Turn on"/"Turn off" on phone peripheral.
     */
    enableStateChange(): void;

    /**
     * Unsubscribe for events such "Turn on"/"Turn off" on phone peripheral.
     */
    disableStateChange(): void;
}

export default TurboModuleRegistry.get<Spec>(
    'ReactNativeBluetoothLe',
) as Spec | null;
