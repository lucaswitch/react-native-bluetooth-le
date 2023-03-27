package com.rnbluetoothle.kotlin;

import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Build.VERSION;

/**
 * Deal with bluetooth  adapter device state.
 */
class BluetoothAdapterState {

    /**
     * State constants.
     **/
    companion object {
        const val ADAPTER_CHANGED_ACTION = "ADAPTER_CHANGED_ACTION";
        const val ADAPTER_STATUS_ON = 2;
        const val ADAPTER_STATUS_TURNING_ON = 1;
        const val ADAPTER_STATUS_OFF = -1;
        const val ADAPTER_STATUS_TURNING_OFF = 0;


        /**
         * Gets bluetooth adapter.
         */
        fun getAdapter(): BluetoothAdapter? {
            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
            return bluetoothManager.getAdapter();
        }

        /**
         * Gets whether bluetooth is supported.
         */
        fun getIsSupported(): Boolean {
            return getAdapter() !== null;
        }

        /**
         * Gets whether device bluetooth is enabled or not.
         * In case of device does not have support for bluetooth capabilities returns false.
         * @return Boolean
         */
        fun getIsEnabled(): Boolean {
            val bluetoothAdapter: BluetoothAdapter? = getAdapter();
            return !bluetoothAdapter == null && bluetoothAdapter.isEnabled;
        }

        /**
         * Turns on bluetooth if possible.
         */
        fun turnOnBluetoothIfPossible() {
            if (!getIsEnabled()) {
                val requestEnableBluetoothHardwareIntent =
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(requestEnableBluetoothHardwareIntent, REQUEST_ENABLE_BT)
            }
        }

        /**
         * Turns off bluetooth if possible.
         * Only possible on SDK < 33
         */
        fun turnOffBluetoothIfPossible() {

        }
    }
}