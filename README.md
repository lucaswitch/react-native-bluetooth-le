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
import {turnOnBluetoothIfPossible, turnOffBluetoothIfPossible, onDiscovery, onStateChange} from 'react-native-bluetooth-le';

turnOnBluetoothIfPossible(); // The bluetooth adapter was turned on.
turnOffBluetoothIfPossible(); // The bluetooth adapter was turned off.

/** Listen for bluetooth power state change */
const unsubscribe = onStateChange(({status})=>{
  console.log(status); // "on" or "off"
})

// Later when does not need to listen this event anymore...
unsubscribe();


/** Listen bluetooth discovery event  */
const unsubscribe = onDiscovery((device)=>{
  console.log(device);
})

// Later when does not need to listen this event anymore...
unsubscribe();

```

### Docs
  The following methods are provided:
    
#### Whether bluetooth adapter is enabled
    
```js
  import {getIsEnabled} from 'react-native-bluetooth-le';

  getIsEnabled(); // "on" or "off"

```

#### Gets whether bluetooth adapter has low energy support
  
```js
  import {getIsSupported} from 'react-native-bluetooth-le';

  getIsSupported(); // true or false, true if is current device supports bluetooth low energy operations.
```

#### Gets bluetooth adapter name and address

  For the user security, make sure to ask the user common bluetooth permissions.
  
```js
  import {getName, getAddress} from 'react-native-bluetooth-le';

  getName(); // "Samsung S21..."
  getAddress(); // "00:00:00:00:21" Mac address for Android and UUID for some most recent IOS versions.
```

#### Gets whether location is enabled 

  In some operations it's necessary to access user location. To know if user already authorized location fetching use the following method:
  
```js
  import {getIsLocationEnabled} from 'react-native-bluetooth-le';

  getIsLocationEnabled(); // "on" or "off"

```

#### Listen to bluetooth adapter sudden state change

  In some operations it's necessary to access user location. To know if user already authorized location fetching use the following method:
  
```js
  import {onStateChange} from 'react-native-bluetooth-le';

  const unsubscribe = onStateChange(({status})=>{
    console.log(status): // "on" or "off"
  }); 

  // Make sure to "unsubscribe" this event later to avoid listen unnecessary state changes.
  
  unsubscribe(); // Remove registered listener.
```

#### Listen bluetooth discovery and find nearby devices

  Starts to listen nearby devices using "bluetooth scan".
  Make sure to enable this functionality on your app for a small period of time since it's consumes too much battery.
  Android Notes:
      Make sure to ask the user to enable location since bluetooth scan operations can expose user location to nearby devices. 
```js
  import {onDiscovery} from 'react-native-bluetooth-le';

 
  const unsubscribe = onDiscovery((devices = [])=>{
     console.log(devices); // This devices is filtered by it's mac address and never will be sent repeated.
  }); 

  // Make sure to "unsubscribe" this event later to avoid listen discovery.  
  unsubscribe(); // Remove registered listener.
```

  
You can find more on our website. :)

## Capabilites

- Bluetooth Adapter
    - Verify if device has a bluetooth adapter available and is bluetooth low energy capable. [**Supported**]
    - Verify if bluetooth adapter is currently enabled. [**Supported**]
    - Listen to bluetooth peripheral events such *turn off*, *turn on*. [**Supported**]
- GATT (Generic Attribute Profile) Operations 
    - Discover [**Supported**]
        - Allow the device to find out nearby devices.  [**Supported**]
    - Bonding [**Under development**]
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
