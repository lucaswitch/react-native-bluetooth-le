import {useCallback, useEffect, useState} from "react";
import {Bluetooth, BluetoothDevice} from "react-native-bluetooth-le";
import {Text, TouchableOpacity} from "react-native";
import {Container} from "./style";


/**
 * Bonding demo button.
 * @constructor
 */
export function BondingButton({address, bond}: { address: string, bond: string }) {
    const [bondState, setBondState] = useState(bond);
    const [loading, setLoading] = useState(false);

    /**
     * On press device button.
     */
    const handleOnPress = useCallback(async () => {
        if (!loading) {
            if (bondState === 'bonding') {
                setLoading(true);
                try {
                    if (Bluetooth.getIsBonded(address)) { // Maybe the device was bond state has changed on device configuration.
                        await Bluetooth.unBond(address);
                    }
                } catch (err) {
                    console.info(err);
                } finally {
                    setLoading(false);
                }
            } else {
                try {
                    if (!Bluetooth.getIsBonded(address)) {  // Maybe the device was bond state has changed on device configuration.
                        setLoading(true);
                        await Bluetooth.bond(address);
                    }
                } catch (err) {
                    console.info(err);
                } finally {
                    setLoading(false);
                }
            }
        }
    }, [bondState, loading])

    useEffect(() => {
        Bluetooth.onBondChange(address, (device: BluetoothDevice) => {
            setBondState(device.bond);
        })
    }, [address, bond])

    return <Container>
        <TouchableOpacity onPress={handleOnPress} style={{
            padding: 2,
            paddingLeft: 4,
            paddingRight: 4,
            backgroundColor: '#e5e5e5',
            borderStyle: 'solid',
            borderRadius: 4,
            marginLeft: 4
        }}>
            {
                loading ?
                    <Text style={{fontSize: 8}}>
                        {bondState === 'bonding' ? 'Unpairing...' : 'Pairing...'}
                    </Text>
                    :
                    <Text style={{fontSize: 8}}>{bondState === "bonded" ? "Paired" : "Not paired(Press to pair)"}</Text>
            }
        </TouchableOpacity>
    </Container>
};
