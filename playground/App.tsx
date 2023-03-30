import React, {useEffect} from 'react';
import {SafeAreaView} from 'react-native';
import {Button} from 'react-native';
import Bluetooth from 'react-native-bluetooth-le/js/NativeReactNativeBluetoothLe';

function App(): JSX.Element {
  /**
   * Turn on bluetooth peripheral when user interacts.
   */
  const handleOnTurnOnBluetooth = () => {
    Bluetooth?.turnOnIfPossible();
  };

  useEffect(() => {
    if (Bluetooth?.getIsSupported()) {
      console.info('Bluetooth is supported.');
    } else {
      console.info('Bluetooth is not supported.');
    }
  }, []);

  return (
    <SafeAreaView>
      <Button
        title="Tentar ligar o bluetooth"
        onPress={handleOnTurnOnBluetooth}
      />
    </SafeAreaView>
  );
}

export default App;
