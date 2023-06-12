import {Bluetooth, BluetoothDevice} from 'react-native-bluetooth-le';
import {useCallback, useEffect, useState} from 'react';
import {
  Button,
  FlatList,
  PermissionsAndroid,
  Platform,
  Text,
} from 'react-native';
import {Card} from '../../card';
import {ButtonGroup, Container, ListItem, ListItemInside} from './style';
import {OverlineText} from '../../texts';
import {BondingButton} from '../bonding-button';
import {DetailsButton} from '../details-button';

/**
 * Bluetooth discovery demo.
 * @constructor
 */
export function Discovery() {
  const [allowed, setAllowed] = useState(false);
  const [devices, setDevices] = useState<BluetoothDevice[]>([]);
  const [discovering, setDiscovering] = useState<boolean>(false);
  const [enabled, setEnabled] = useState<boolean>(false);

  /**
   * Asks bluetooth permission.
   */
  const askBluetoothPermissions = useCallback(async () => {
    if (Platform.OS === 'android') {
      // Connect permission
      if (
        !(await PermissionsAndroid.check(
          'android.permission.BLUETOOTH_CONNECT',
        ))
      )
        await PermissionsAndroid.request(
          'android.permission.BLUETOOTH_CONNECT',
          {
            title: 'We need permission to access your bluetooth adapter.',
            message: 'To proceed please press "Allow".',
            buttonNegative: 'Cancel',
            buttonPositive: 'Allow',
          },
        );

      // Scan permission
      if (
        !(await PermissionsAndroid.check('android.permission.BLUETOOTH_SCAN'))
      )
        await PermissionsAndroid.request('android.permission.BLUETOOTH_SCAN', {
          title: 'We need permission to access your bluetooth adapter scan.',
          message: 'To proceed please press "Allow".',
          buttonNegative: 'Cancel',
          buttonPositive: 'Allow',
        });

      if (
        !(await PermissionsAndroid.check(
          'android.permission.ACCESS_FINE_LOCATION',
        ))
      ) {
        await PermissionsAndroid.request(
          'android.permission.ACCESS_FINE_LOCATION',
          {
            title:
              'We need permission to access your location to perform a bluetooth discovery.',
            message: 'To proceed please press "Allow".',
            buttonNegative: 'Cancel',
            buttonPositive: 'Allow',
          },
        );
      }
    }
  }, []);

  /**
   * Starts bluetooth discovery.
   */
  const startDiscovery = useCallback(async () => {
    if (!allowed) {
      await askBluetoothPermissions();
      setAllowed(true);
    }
    if (!enabled) {
      setEnabled(true);
    }
  }, [enabled, allowed]);

  /**
   * Stops discovery.
   */
  const stopDiscovery = useCallback(() => {
    // In this point we already know that scan was previously running so the permissions to scan must be ok.
    setEnabled(false);
  }, [enabled]);

  /**
   * Renders a device item.
   */
  const renderItem = useCallback(
    (props: {index: number; item: BluetoothDevice}) => {
      const {address, name, bond} = props.item;

      return (
        <ListItem key={props.index}>
          <ListItemInside>
            <OverlineText style={{color: '#000'}}>
              {`${name} | ${address}`}
            </OverlineText>
            <ButtonGroup>
              <BondingButton address={address} bond={bond} />
              <DetailsButton device={props.item} />
            </ButtonGroup>
          </ListItemInside>
        </ListItem>
      );
    },
    [],
  );

  useEffect(() => {
    // Handle discovery.
    if (allowed && enabled) {
      const unsubscribe = Bluetooth.onDiscovery(setDevices);
      setDiscovering(true);
      return () => {
        setDiscovering(false);
        setDevices([]);
        unsubscribe();
      };
    }
  }, [enabled, allowed]);

  return (
    <Card>
      <Container>
        <Text>Bluetooth devices</Text>
        <OverlineText>
          {discovering
            ? 'Perfoming nearby devices discovery...'
            : 'Press "Find devices to start discovery."'}
        </OverlineText>
        <FlatList
          data={devices}
          renderItem={renderItem}
          scrollEnabled={true}
          style={{height: 200, marginTop: 10}}
        />
        <ButtonGroup>
          {!discovering ? (
            <Button title="Find devices" onPress={startDiscovery} />
          ) : (
            <Button title="Cancel" onPress={stopDiscovery} />
          )}
        </ButtonGroup>
      </Container>
    </Card>
  );
}
