package com.rnbluetoothle.bluetooth.bridge;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

/**
 * A helper class to deal with Js events dispatching.
 */
public class JsEventDispatcher {

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event, WritableMap map) {
        Log.v("Bluetooth", "Sending JS Module event: " + event);
        context.getJSModule(RCTDeviceEventEmitter.class).emit(event, map);
    }

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event, WritableArray arr) {
        Log.v("Bluetooth", "Sending JS Module event: " + event);
        context.getJSModule(RCTDeviceEventEmitter.class).emit(event, arr);
    }

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event, String text) {
        Log.v("Bluetooth", "Sending JS Module event: " + event + " with value " + text);
        context.getJSModule(RCTDeviceEventEmitter.class).emit(event, text);
    }

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event, int value) {
        Log.v("Bluetooth", "Sending JS Module event: " + event + " with value " + value);
        context.getJSModule(RCTDeviceEventEmitter.class).emit(event, value);
    }

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event) {
        Log.v("Bluetooth", "Sending JS Module event: " + event + " with null");
        context.getJSModule(RCTDeviceEventEmitter.class).emit(event, null);
    }

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event, byte[] value) {
        Log.v("Bluetooth", "Sending JS Module event: " + event + " with bytes.");
        WritableArray jsByteArray = Arguments.createArray();
        for (byte currentByte : value) {
            jsByteArray.pushInt(currentByte & 0xFF);
        }
        context.getJSModule(RCTDeviceEventEmitter.class).emit(event, jsByteArray);
    }
}
