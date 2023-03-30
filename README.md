# react-native-bluetooth-le


<img src="https://preview.redd.it/this-is-the-quokka-its-a-marsupial-from-western-australia-v0-f42nstx1tur91.jpg?width=640&crop=smart&auto=webp&s=3bdac83282dbc70f3850d2fdcca6454033f67a57" width="350" alt="accessibility text">

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


## Contributing

```bash
  cd playground;
  yarn add ../lib;
  cd android;
  ./gradlew generateCodegenArtifactsFromSchema;
  cd ..;
  yarn run android --active-arch-only;
```
