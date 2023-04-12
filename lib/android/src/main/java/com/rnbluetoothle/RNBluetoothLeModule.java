package com.rnbluetoothle;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.rnbluetoothle.NativeReactNativeBluetoothLeSpec;
import com.rnbluetoothle.bluetooth.BluetoothState;
import com.rnbluetoothle.bluetooth.receivers.*;

public class RNBluetoothLeModule extends NativeReactNativeBluetoothLeSpec {

  public static String NAME = "ReactNativeBluetoothLe";
  private ReactApplicationContext reactContext;

  /**
   * Receiver that listen to Bluetooth core events in this module.
   * This receiver only listen intents when the host is on responsive state and the UI is visible.
   */
  private GlobalReceiver globalReceiver;

  RNBluetoothLeModule(ReactApplicationContext context) {
    super(context);
    this.reactContext = context;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  /**
   * Gets whether bluetooth is supported.
   *
   * @return
   */
  @Override
  public boolean getIsSupported() {
    return true;
  }

  /**
   * Gets whether bluetooth is supported.
   *
   * @return
   */
  @Override
  public void turnOnIfPossible() {}

  /**
   * Gets whether bluetooth is supported.
   *
   * @return
   */
  @Override
  public void turnOffIfPossible() {}

  /**
   * Starts broadcast receivers.
   */
  private void registerGlobalBroadcast() {
    // Register global listener.
    if (globalReceiver == null) {
      globalReceiver = new GlobalReceiver(this.reactContext);
      reactContext.registerReceiver(
        globalReceiver,
        GlobalReceiver.createIntentFilter()
      );
      Log.v("Bluetooth", "\"GlobalReceiver\" registered receiver.");
    }
  }

  /**
   * Stops broadcast receiver.
   */
  private void unregisterGlobalBroadcast() {
    if (globalReceiver != null) {
      reactContext.unregisterReceiver(globalReceiver);
      globalReceiver = null;
    }
  }

  /**
   * Add a JS module event listener.
   */
  @Override
  public void addListener(String eventName) {
    Log.v("Bluetooth", "Received addListener on event: "+ eventName);
    registerGlobalBroadcast();
    globalReceiver.enableEvent(eventName);
  }

  /**
   * Remove a JS module event listener.
   */
  @Override
  public void removeListener(String eventName){
    Log.v("Bluetooth", "Received removeListener on event: "+ eventName);
    globalReceiver.disableEvent(eventName);
    if(globalReceiver.getEventsCount() == 0){
        unregisterGlobalBroadcast();
    }
  }
}
