# react-native-bluetooth-le


<img src="https://github.com/lucaswitch/react-native-bluetooth-le/blob/feature/android/logo.png" width="128" alt="accessibility text">

## Introduction

This react-native library focus on implement common bluetooth operations in react-native ecosystem.


## Installation 
  Using yarn
  ```
    yarn add react-native-bluetooth-le
  ```
  Using npm
  ```
    npm install react-native-bluetooth-le
  ```


## Imperative API

```js
  import {turnOnBluetoothIfPossible, turnOffBluetoothIfPossible} from 'react-native-bluetooth-le';
  
  
  turnOnBluetoothIfPossible(); // The bluetooth pheripheral was turned on.
  turnOffBluetoothIfPossible(); // The bluetooth pheripheral was turned off.
```

## Capabilites
  - Bluetooth Adapter
    - Listen to bluetooth peripheral events such Turn Off and Turn On.
    - Get current Bluetooth peripheral status.
   - GATT
    - Discovery 
      - Start discovery and listen to scan events.
      - Disable discovery and unsubscribe to scan events.
    - Bonding
      - Bond with a device.   
      - Connect to device.
      - Discover device services.
      - Discover service characteristics.
      - Listen to disconnection from a device.     
  - Transmission
      - Receive notifications from a device service and characteristic.
      - Send data to device.

## Contributing

```bash
  cd playground;
  yarn add ../lib;
  cd android;
  ./gradlew generateCodegenArtifactsFromSchema;
  cd ..;
  yarn run android --active-arch-only;
```
