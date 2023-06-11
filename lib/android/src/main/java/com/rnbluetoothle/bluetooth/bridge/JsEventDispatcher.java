package com.rnbluetoothle.bluetooth.bridge;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * A helper class to deal with Js events dispatching.
 */
public class JsEventDispatcher {

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext reactContext, String event, WritableMap map) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(event, map);
        Log.v("Bluetooth", "Sending JS Module event: " + event);
    }

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event, WritableArray arr) {
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(event, arr);
        Log.v("Bluetooth", "Sending JS Module event: " + event);
    }

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event, String text) {
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(event, text);
        Log.v("Bluetooth", "Sending JS Module event: " + event + " with value " + text);
    }

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event, int value) {
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(event, value);
        Log.v("Bluetooth", "Sending JS Module event: " + event + " with value " + value);
    }

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event) {
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(event, null);
        Log.v("Bluetooth", "Sending JS Module event: " + event + " with null");
    }

    /**
     * Sends the JS event.
     */
    public static void send(ReactApplicationContext context, String event, byte[] value) {
        WritableArray jsByteArray = Arguments.createArray();
        for (byte currentByte : value) {
            jsByteArray.pushInt(currentByte & 0xFF);
        }
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(event, jsByteArray);
        Log.v("Bluetooth", "Sending JS Module event: " + event + " with bytes.");
    }
}
