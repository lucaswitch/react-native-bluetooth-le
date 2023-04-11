package com.rnbluetoothle.receivers;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactContext;


/**
 * Global receiver responsible to receive this library broadcast messages and emit to ReactNativeContext.
 */
public class GlobalReceiver extends BroadcastReceiver {
    private Runnable callback;

    public GlobalReceiver(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("Bluetooth", "[GlobalReceiver] Received a intent.");
        if (intent.getAction().equals(BluetoothStateReceiver.ACTION_STATE_CHANGE)) {
            String status = intent.getStringExtra("status");
            Log.v("Bluetooth", "Bluetooth status just changed to: " + status);

            WritableMap payload = Arguments.createMap();
            payload.putString("status", status);

            Log.v("Bluetooth", context.getClass().getName());
            this.callback.run("onStageChange", payload);
        }
    }

    /**
     * Create a IntentFilter with corresponding receiver supported actions.
     *
     * @return
     */
    public static IntentFilter createIntentFilter() {
        IntentFilter createdIntentFilter = new IntentFilter();
        createdIntentFilter.addAction(BluetoothStateReceiver.ACTION_STATE_CHANGE);
        return createdIntentFilter;
    }
}