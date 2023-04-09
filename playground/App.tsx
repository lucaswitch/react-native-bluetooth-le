import React, {useEffect, useState} from 'react';
import {SafeAreaView} from 'react-native';
import {Button} from 'react-native';
import {Bluetooth} from 'react-native-bluetooth-le/js/index';

function App(): JSX.Element {
  /**
   * Turn on bluetooth peripheral when user interacts.
   */
  const handleOnTurnOnBluetooth = () => {};

  useEffect(() => {
    console.log('running');
    const unsubscribe = Bluetooth.subscribeStateChange(function ({status}) {
      console.log('status', status);
    });
    return () => {
      unsubscribe();
    };
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
