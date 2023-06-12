import {Bluetooth} from 'react-native-bluetooth-le';
import {useCallback, useEffect, useState} from 'react';
import {PermissionsAndroid, Platform, Text} from 'react-native';
import {Card} from '../../card';
import {Container} from './style';
import {OverlineText} from '../../texts';

/**
 * Bluetooth state demo.
 * @constructor
 */
export function BluetoothState() {
  const [allowed, setAllowed] = useState(false);
  const [state, setState] = useState<string>('');
  const [name, setName] = useState<string>('');
  const [address, setAddress] = useState<string>('');

  /**
   * Asks bluetooth permission.
   */
  const askBluetoothPermissions = useCallback(async () => {
    if (Platform.OS === 'android') {
      if (
        await PermissionsAndroid.check('android.permission.BLUETOOTH_CONNECT')
      ) {
        return;
      }
      await PermissionsAndroid.request('android.permission.BLUETOOTH_CONNECT', {
        title: 'We need permission to access your bluetooth adapter.',
        message: 'To proceed please press "Allow".',
        buttonNegative: 'Cancel',
        buttonPositive: 'Allow',
      });
    }
  }, []);

  useEffect(() => {
    if (!allowed) {
      askBluetoothPermissions()
        .then(() => {
          setAllowed(true);
        })
        .catch(() => {
          setAllowed(false);
        });
    }
  }, [allowed, askBluetoothPermissions]);

  useEffect(() => {
    // Listen to any bluetooth power status.
    if (allowed) {
      const unsubscribe = Bluetooth.onStateChange(({status}) => {
        setState(status);
        console.log('status', status);
      });

      setName(Bluetooth.getName());
      setAddress(Bluetooth.getAddress());
      return () => {
        unsubscribe();
      };
    }
  }, [allowed]);

  return (
    <>
      <Card>
        <Container>
          <Text style={{flex: 1}}>
            Bluetooth Adapter ({state === 'on' ? 'on' : 'off'})
          </Text>
        </Container>
        <OverlineText>
          {state === 'turning_on'
            ? 'Bluetooth adapter is turning on...'
            : state === 'turning_off'
            ? 'Bluetooth adapter is turning off...'
            : state === 'on'
            ? 'Bluetooth adapter is on and ready.'
            : 'Bluetooth adapter is off.'}
        </OverlineText>
        <OverlineText>
          Now discoverable as {name} and at address {address}
        </OverlineText>
      </Card>
    </>
  );
}
