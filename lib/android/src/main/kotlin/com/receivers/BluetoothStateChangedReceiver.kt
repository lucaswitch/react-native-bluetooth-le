package com.rnbluetoothle.kotlin.receivers;

import com.rnbluetoothle.kotlin.BluetoothAdapterState;

/**
 * Broadcast Receiver who listens bluetooth adapter state changes.
 */
class BluetoothStateChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        val eventIntent = Intent(
                            BluetoothAdapterState.ADAPTER_CHANGED_ACTION
                        );
                        eventIntent.putExtra("status", BluetoothAdapterState.ADAPTER_STATUS_OFF);
                        context.sendBroadcast(resultIntent);
                    }

                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        val eventIntent = Intent(
                            BluetoothAdapterState.ADAPTER_CHANGED_ACTION
                        );
                        eventIntent.putExtra(
                            "status",
                            BluetoothAdapterState.ADAPTER_STATUS_TURNING_OFF
                        );
                        context.sendBroadcast(resultIntent);
                    }

                    BluetoothAdapter.STATE_ON -> {
                        val eventIntent = Intent(
                            BluetoothAdapterState.ADAPTER_CHANGED_ACTION
                        );
                        eventIntent.putExtra("status", BluetoothAdapterState.ADAPTER_STATUS_ON);
                        context.sendBroadcast(resultIntent);
                    }

                    BluetoothAdapter.STATE_TURNING_ON -> {
                        val eventIntent = Intent(
                            BluetoothAdapterState.ADAPTER_CHANGED_ACTION
                        );
                        eventIntent.putExtra(
                            "status",
                            BluetoothAdapterState.ADAPTER_STATUS_TURNING_ON
                        );
                        context.sendBroadcast(resultIntent);
                    }
                }
            }
        }
    }
}