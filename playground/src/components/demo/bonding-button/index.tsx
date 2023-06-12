import {useCallback, useState} from 'react';
import {Bluetooth} from 'react-native-bluetooth-le';
import {Alert, Text, TouchableOpacity} from 'react-native';
import {Container} from './style';

/**
 * Bonding demo button.
 * @constructor
 */
export function BondingButton({
  address,
  bond,
}: {
  address: string;
  bond: string;
  onChange?: () => void;
}) {
  const [bondState, setBondState] = useState(bond);
  const [loading, setLoading] = useState(false);

  /**
   * On press device button.
   */
  const handleOnPress = useCallback(async () => {
    if (!loading) {
      setLoading(true);
      try {
        if (bondState === 'bonded') {
          if (Bluetooth.getIsBonded(address)) {
            // Maybe the device was bond state has changed on device configuration.
            await Bluetooth.unBond(address);
          }
        } else {
          if (!Bluetooth.getIsBonded(address)) {
            // Maybe the device was bond state has changed on device configuration.
            setLoading(true);
            await Bluetooth.bond(address);
          }
        }
      } catch (err) {
        console.error(err);
        Alert.alert(
          'A error has ocurred',
          `Something just went wrong, :/ ${err}`,
        );
      }
      setLoading(false);
    }
  }, [address, bondState, loading]);

  return (
    <Container>
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
        {loading ? (
          <Text style={{fontSize: 8}}>
            {bondState === 'bonded' ? 'Unpairing...' : 'Pairing...'}
          </Text>
        ) : (
          <Text style={{fontSize: 8}}>
            {bondState === 'bonded' ? 'Paired' : 'Not paired(Press to pair)'}
          </Text>
        )}
      </TouchableOpacity>
    </Container>
  );
}
