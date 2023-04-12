package com.rnbluetoothle.bluetooth.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import java.util.HashMap;

/**
 * Global receiver responsible to receive this library broadcast messages and emit to ReactNativeContext.
 */
public class GlobalReceiver extends BroadcastReceiver {

  private ReactApplicationContext reactContext;
  private HashMap<String, String> enabledEvents = new HashMap<String, String>();

  public GlobalReceiver(ReactApplicationContext reactContext) {
    super();
    reactContext = reactContext;
  }

  /**
   * Sends event back to coupled JS Module.
   */
  private void sendJsModuleEvent(String event, WritableMap map) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(event, map);
  }

  /**
   * Create WritableMap accordingly to "ACTION_STATE_CHANGED" intent.
   */
  @Nullable
  private WritableMap createNativeMapForBluetoothStateChangeIntent(
    Intent intent
  ) {
    WritableMap payload = Arguments.createMap();

     BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    int state = bluetoothAdapter.getState();
    if (state == BluetoothAdapter.STATE_OFF) {
      payload.putString("status", "off");
      return payload;
    } else if (state == BluetoothAdapter.STATE_ON) {
      payload.putString("status", "on");
      return payload;
    }

    return null;
  }

  /**
   * Create a IntentFilter with corresponding receiver supported actions.
   *
   * @return
   */
  public static IntentFilter createIntentFilter() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
    return filter;
  }

  /**
   * Gets the event name with correct namespace.
   */
  public static String createEventName(String event) {
    return "rnbluetoothle." + event;
  }

  /**
   * Enable events.
   */
  public void enableEvent(String event) {
    enabledEvents.put(createEventName(event), event);
  }

  /**
   * Disable event.
   */
  public void disableEvent(String event) {
    enabledEvents.remove(createEventName(event));
  }

  /**
   * Gets events count.
   */
  public int getEventsCount() {
    return enabledEvents.size();
  }

  /**
   * Register this broadcast receiver.
   */
  public void register() {
    reactContext.registerReceiver(this, createIntentFilter());
    Log.v("Bluetooth", "\"GlobalReceiver\" registered.");
  }

  /**
   * Unregister this broadcast receiver.
   */
  public void unregister() {
    reactContext.unregisterReceiver(this);
  }

  /**
   * On receive a Intent.
   */
  @Override
  public void onReceive(Context context, Intent intent) {
    if (enabledEvents.size() > 0) {
      final String className = context.getClass().getName();
      String action = intent.getAction();
      Log.v("Bluetooth", className + " received a intent: " + action);

      switch (action) {
        case BluetoothAdapter.ACTION_STATE_CHANGED: // Bluetooth state just changed to On or Off.
          String eventName = createEventName("onStateChange");
          if (enabledEvents.containsKey(eventName)) {
            this.sendJsModuleEvent(
                eventName,
                createNativeMapForBluetoothStateChangeIntent(intent)
              );
          }
          break;
        default:
          Log.v("Bluetooth", className + " received a intent: " + action);
          break;
      }
    }
  }
}
