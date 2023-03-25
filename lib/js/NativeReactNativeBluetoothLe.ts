import type {TurboModule} from 'react-native/Libraries/TurboModule/RCTExport';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
    /**
     * Turn on Bluetooth Module of device.
     */
    turnOnIfPossible(): Promise<boolean>;

    /**
     * Turn off Bluetooth Module of device.
     */
    turnOffIfPossible(): Promise<boolean>;
}

export default TurboModuleRegistry.get<Spec>(
    'ReactNativeBluetoothLe',
) as Spec | null;
