package com.rnbluetoothle.bluetooth.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;

import com.rnbluetoothle.bluetooth.bridge.JsBluetoothState;
import com.rnbluetoothle.bluetooth.bridge.JsEventDispatcher;
import com.rnbluetoothle.bluetooth.receivers.TransactionReceiver;


/**
 * Responsible to deal with adapter state change events.
 */
public class AdapterStateChangeReceiver extends TransactionReceiver {
    protected String EVENT_ON_STATE_CHANGE = "rnbluetoothle.onStateChange ";

    public AdapterStateChangeReceiver(ReactApplicationContext context, String transactionId) {
        super(context, transactionId);
        this.intentActions = new String[]{BluetoothAdapter.ACTION_STATE_CHANGED};
        this.EVENT_ON_STATE_CHANGE = this.EVENT_ON_STATE_CHANGE + transactionId;
    }

    /**
     * On receive adapter state change intent events.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        JsEventDispatcher.send(
                this.reactContext,
                this.EVENT_ON_STATE_CHANGE,
                JsBluetoothState.getMap(this.reactContext)
        );
    }
}