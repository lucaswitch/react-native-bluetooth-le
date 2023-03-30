import type {TurboModule} from 'react-native/Libraries/TurboModule/RCTExport';
import {TurboModuleRegistry} from 'react-native';

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
}

export default TurboModuleRegistry.get<Spec>(
    'ReactNativeBluetoothLe',
) as Spec | null;
