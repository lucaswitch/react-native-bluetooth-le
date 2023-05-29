package com.rnbluetoothle.bluetooth.receivers;

import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;

import com.rnbluetoothle.bluetooth.bridge.JsEventDispatcher;
import com.rnbluetoothle.bluetooth.bridge.JsBluetoothState;

/**
 * Responsible to deal with adapter state change events.
 */
public class StateReceiver extends TransactionReceiver {
    protected EVENT_ON_STATE_CHANGE ="rnbluetoothle.onStateChange/";

    public StateReceiver(ReactApplicationContext context, String transactionId) {
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
    public onReceiver(Context context, Intent intent) {
        JsEventDispatcher.send(this.EVENT_ON_STATE_CHANGE, JsBluetoothState.getMap());
    }
}