// @ts-ignore
import type {TurboModule} from 'react-native/Libraries/TurboModule/RCTExport';
// @ts-ignore
import {TurboModuleRegistry} from 'react-native';
import {BluetoothConnectionPriority, BluetoothDevice, BluetoothDeviceService} from "./types";

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
     * Gets whether bluetooth default system adapter is enabled.
     * It returns always false if is enabled and does not support Bluetooth Low Energy.
     * Use "getIsSupported" method to check if the current running device has a Bluetooth Low Energy Adapter
     */
    getIsEnabled(): boolean

    /**
     * Gets whether location is enabled for the application.
     *
     * On Android:
     *  Some bluetooth operations in Android can expose the user location, so to work you must check if user consents its location.
     *  Make sure that the permission "android.permission.ACCESS_FINE_LOCATION" is granted.
     */
    getIsLocationEnabled(): boolean

    /**
     * Gets whether device has a bluetooth adapter and supports bluetooth low energy.
     * On Android:
     *  Make sure to ask the android.bluetooth needed permissions.
     */
    getIsSupported(): boolean;

    /**
     * Gets system bluetooth adapter name.
     * On android:
     *   It maybe returns a empty string if the application could not find a system supported bluetooth adapter for low energy.
     *   To avoid it call "getIsSupported" to verify if has indeed a bluetooth adapter before calling this method.
     */
    getAdapterName(): string;

    /**
     * Gets system bluetooth adapter address.
     * On android:
     *   It maybe returns a empty string if the application could not find a system supported bluetooth adapter for low energy.
     *   To avoid it call "getIsSupported" to verify if has indeed a bluetooth adapter before calling this method.
     */
    getAdapterAddress(): string;

    /**
     * Gets whether de following device id is bonding.
     * On Android:
     *  The id is the remote device mac address.
     */
    getIsBonded(id: string): boolean

    /**
     * Creates a bond with device id.
     * Promises fulfills when the bond is sucessfully started.
     * @param id
     * @return string Returns error message or empty string if successful.
     */
    bond(id: string): string;

    /**
     * Destroys a bond with device id.
     * Promises fulfills when the bond is sucessfully destroyed.
     * To bond with device look "bond" method.
     * @param id
     * @return string Returns error message or empty string if successful.
     */
    unBond(id: string): string;

    /**
     * Get the current bonding devices.
     */
    getBondedDevices(): BluetoothDevice[];

    /**
     * Gets if device is connected.
     * @param id The device id.
     */
    getIsConnected(id: string): boolean;

    /**
     * Stablish a GATT connection with remote device.
     */
    connect(id: string, priority?: BluetoothConnectionPriority): Promise<void>;

    /**
     * Removes the current GATT connection with remote device.
     */
    disconnect(): Promise<void>;

    /**
     * Add a event listener to native.
     * Only for this library internal use! Stay away and avoid problems.
     * @param eventName
     */
    addListener(eventName: string): void;

    /**
     * Remove a event listener to native.
     * Only for this library internal use! Stay away and avoid problems.
     * @param eventName
     */
    removeListener(eventName: string): void;
}

export default TurboModuleRegistry.get<Spec>(
    'ReactNativeBluetoothLe',
) as Spec | null;
