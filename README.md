# React Native Bluetooth Low Energy

## Introduction

**react-native-bluetooth-le** library stands for implement commons bluetooth low energy specifications in react-native
ecosystem.\
It uses the new react-native **turbo module architecture** thats powers a better perfomance for react-native
applications providing a typescript/javascript api with easy
subscribe/unsubscribe pattern approach to handle bluetooth
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
