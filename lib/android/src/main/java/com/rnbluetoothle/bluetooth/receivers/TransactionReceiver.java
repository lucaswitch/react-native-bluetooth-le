package com.rnbluetoothle.bluetooth.receivers;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;

/*
 * Responsible to deal with transactions based receivers.
 * A transaction receiver must have a string that identifies itself.
 */
public class TransactionReceiver extends BroadcastReceiver {

    protected String[] intentActions = {};
    final protected String transactionId;
    final protected ReactApplicationContext reactContext;

    public TransactionReceiver(ReactApplicationContext context, String transactionId) {
        this.reactContext = context;
        this.transactionId = transactionId;
    }

    /**
     * Creates the intent filter.
     */
    protected IntentFilter createIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        for (int i = 0; i < this.intentActions.length; i++) {
            intentFilter.addAction(this.intentActions[i]);
        }
        return intentFilter;
    }

    /**
     * Register this broadcast receiver.
     */
    public void register() {
        this.reactContext.registerReceiver(this, this.createIntentFilter());
        Log.v("Bluetooth", "Events registered for transaction " + this.transactionId + " for class " + this.getClass().getName());
    }

    /**
     * Unregister this broadcast receiver.
     */
    public void unregister() {
        this.reactContext.unregisterReceiver(this);
        Log.v("Bluetooth", "Events unregistered for transaction " + this.transactionId);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    }
}
