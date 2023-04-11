package com.rnbluetoothle.receivers;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.modules.core.DeviceEventManagerModule;


import androidx.annotation.Nullable;

/**
 * Global receiver responsible to receive this library broadcast messages and emit to ReactNativeContext.
 */
public class GlobalReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("Bluetooth", "[LazyReceiver] Received a intent.");
        if (intent.getAction().equals(BluetoothStateReceiver.ACTION_STATE_CHANGE)) {
            String status = intent.getStringExtra("status");
            Log.v("Bluetooth", "Bluetooth status just changed to: " + status);

            WritableMap payload = Arguments.createMap();
            payload.putString("status", status);
            Log.v("Bluetooth", context.getClass().getName());
            //emitReactContextEvent(context, "onStateChange", payload);
        }
    }

    /**
     * Emits a ReactContext event.
     *
     * @param rContext
     * @param eventName
     * @param args
     */
    public void emitReactContextEvent(ReactApplicationContext rContext, String eventName, @Nullable WritableMap args) {
        rContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, args);
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