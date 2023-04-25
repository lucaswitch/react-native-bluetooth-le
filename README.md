# React Native Bluetooth Low Energy


<img src="https://github.com/lucaswitch/react-native-bluetooth-le/blob/feature/android/logo.png" width="128" alt="accessibility text">

## Introduction

**react-native-bluetooth-le** library stands for implement commons bluetooth low energy specifications in react-native
ecosystem.\
It uses the new react-native **turbo module architecture** thats powers a better performance for react-native
applications. Exposes a bluetooth javascript api with easy subscribe/unsubscribe pattern approach to handle bluetooth
events and provides **foreground mode** transmission
on android.

### Compatibility

Must be a react-native 0.69 or above.\
Must be hermes engine enabled. please checkout the link: https://reactnative.dev/docs/new-architecture-intro to enable.

- Android **(Fully supported on sdk 28 or above)**.
- IOs **(Not yet supported)**

### Getting started

Using yarn

  ```bash
    yarn add react-native-bluetooth-le;
  ```

Using npm

  ```bash
    npm install react-native-bluetooth-le;
  ```

### Imperative API

```js
import {turnOnBluetoothIfPossible, turnOffBluetoothIfPossible} from 'react-native-bluetooth-le';

turnOnBluetoothIfPossible(); // The bluetooth pheripheral was turned on.
turnOffBluetoothIfPossible(); // The bluetooth pheripheral was turned off.
```

### Docs

    You can find more on our website.

## Capabilites

- Bluetooth Adapter
    - Verify if device has a bluetooth adapter available and is bluetooth low energy capable.
    - Verify if bluetooth adapter is currently enabled.
    - Listen to bluetooth peripheral events such *turn off*, *turn on*.
- GATT (Generic Attribute Profile) Operations
    - Discover
        - Allow the device to find out nearby devices.
    - Bonding
        - Allow *bond* with a bluetooth peripheral.
        - Allow *unbound* with *bounded* bluetooth peripheral.
        - Listen to *bound* and *unbound* events.
    - Connection
        - Allow connect to bluetooth peripheral.
        - Allow disconnect from connected bluetooth peripheral.
        - Listen to "*connect*" and "*disconnect*" events.
    - Device profile
        - Allow discover bluetooth peripheral services and it characteristics.
        - Characteristic
            - Receive notifications from a device service and characteristic.
            - Send data to device characteristic using plain bytes structure.
            - Listen to device characteristic data using plain bytes structure.
## Changelog
    - 0.1 (Beta)
        Add support to basic capabilities

## Contributing

```bash
  cd playground;
  yarn add ../lib;
  cd android;
  ./gradlew generateCodegenArtifactsFromSchema;
  cd ..;
  yarn run android --active-arch-only;
```
