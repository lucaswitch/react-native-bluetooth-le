import {
  ButtonGroup,
  Container,
  ModalContainer,
  PropertiesContainer,
  PropertyName,
  PropertyValue,
  PropertyWrapper,
} from './style';
import {Bluetooth, BluetoothDevice} from 'react-native-bluetooth-le';
import {
  Alert,
  Animated,
  Button,
  Modal,
  StatusBar,
  Text,
  TouchableOpacity,
} from 'react-native';
import {useCallback, useEffect, useState} from 'react';
import add = Animated.add;

/**
 * Details about bluetooth remote device.
 * @constructor
 */
export function DetailsButton({device}: {device: BluetoothDevice}) {
  const [open, setOpen] = useState(false);
  const [connected, setConnected] = useState(false);

  /**
   * On press detail button.
   */
  const handleOnPress = useCallback(() => {
    setOpen(!open);
  }, [open]);

  /**
   * Connect device.
   */
  const connect = useCallback(async () => {
    if (device) {
      try {
        await Bluetooth.connect(device.address);
      } catch (err) {
        Alert.alert('It was no possible to connect into device.');
      }
    }
  }, [device]);

  /**
   * Disconnect device.
   */
  const disconnect = useCallback(async () => {
    if (device) {
      try {
        await Bluetooth.disconnect(device.address);
      } catch (err) {
        Alert.alert('It was no possible to disconnect from device.');
      }
    }
  }, [device]);

  const properties = [];
  if (device) {
    for (const property in device) {
      // @ts-ignore
      properties.push([property, device[property]]);
    }
  }

  useEffect(() => {
    if (device) {
      if (Bluetooth.getIsConnected(device.address)) {
        setConnected(true);
      }
    }
  }, [connect, device]);

  useEffect(() => {
    const unsubscribe = Bluetooth.onConnectionChange(device.id, ({status}) => {
      console.log(status);
      setConnected(status === 'connected');
    });

    return () => {
      unsubscribe();
    };
  }, [device.id]);

  return (
    <Container>
      <Modal visible={open}>
        <StatusBar backgroundColor="#ffffff" />
        <ModalContainer>
          <TouchableOpacity onPress={handleOnPress} style={{padding: 6}}>
            <Text style={{fontSize: 12}}>{'<<'} Back to home</Text>
          </TouchableOpacity>
          <PropertiesContainer>
            <PropertyWrapper>
              <PropertyName>connected:</PropertyName>
              <PropertyValue>
                {connected ? 'connected' : 'disconnected'}
              </PropertyValue>
            </PropertyWrapper>
            {properties.map(function ([property, value]) {
              return (
                <PropertyWrapper key={property}>
                  <PropertyName>{property}:</PropertyName>
                  <PropertyValue>{value}</PropertyValue>
                </PropertyWrapper>
              );
            })}
          </PropertiesContainer>
          <ButtonGroup>
            {connected ? (
              <Button title="Disconnect" onPress={disconnect} />
            ) : (
              <Button title="Connect" onPress={connect} />
            )}
          </ButtonGroup>
        </ModalContainer>
      </Modal>
      <TouchableOpacity
        onPress={handleOnPress}
        style={{
          padding: 2,
          paddingLeft: 4,
          paddingRight: 4,
          backgroundColor: '#e5e5e5',
          borderStyle: 'solid',
          borderRadius: 4,
          marginLeft: 4,
        }}>
        <Text style={{fontSize: 8}}>Details</Text>
      </TouchableOpacity>
    </Container>
  );
}
