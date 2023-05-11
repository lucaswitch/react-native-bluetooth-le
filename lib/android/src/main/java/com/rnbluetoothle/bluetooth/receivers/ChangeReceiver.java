package src.main.java.com.rnbluetoothle.bluetooth.receivers;

import static android.os.Build.VERSION_CODES;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.Log;

import src.main.java.com.rnbluetoothle.bluetooth.JsBluetoothDevice;

/**
 * Deals with a single device connection changes events.
 */
public class ChangeReceiver extends com.rnbluetoothle.bluetooth.receivers.DeviceEventsReceiver {
    final String EVENT_ON_CHANGE = "rnbluetoothle.onChange";

    public ChangeReceiver(com.facebook.react.bridge.ReactApplicationContext context, String deviceId) {
        super(context, deviceId);
        this.intentActions = new String[]{
                BluetoothDevice.ACTION_CLASS_CHANGED,
                BluetoothDevice.ACTION_NAME_CHANGED
        };
    }

    /**
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        BluetoothDevice device;
        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice.class);
        } else {
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        }

        final String address = device.getAddress();
        if (address.equals(device.getAddress())) {

            String eventName = this.EVENT_ON_CHANGE + "/" + address;
            JsBluetoothDevice jsBluetoothDevice = new JsBluetoothDevice(intent);
            this.sendJsModuleEvent(eventName, jsBluetoothDevice.getMap());
            Log.v("Bluetooth", "Remote " + address + "device just changed.");
        }
    }
}
