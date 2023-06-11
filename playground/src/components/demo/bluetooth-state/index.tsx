import {Bluetooth} from "react-native-bluetooth-le";
import {useCallback, useEffect, useState} from "react";
import {PermissionsAndroid, Platform, Text} from "react-native";
import {Card} from "../../card";
import {Container} from "./style";
import {OverlineText} from '../../texts'


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
            if (await PermissionsAndroid.check('android.permission.BLUETOOTH_CONNECT')) {
                return;
            }
            await PermissionsAndroid.request('android.permission.BLUETOOTH_CONNECT', {
                title: 'We need permission to access your bluetooth adapter.',
                message: 'To proceed please press "Allow".',
                buttonNegative: 'Cancel',
                buttonPositive: 'Allow',
            })
        }
    }, [])

    useEffect(() => {
        if (!allowed) {
            askBluetoothPermissions()
                .then(() => {
                    setAllowed(true)
                })
                .catch(() => {
                    setAllowed(false)
                })
        }
    }, [allowed, askBluetoothPermissions])

    useEffect(() => {
        // Listen to any bluetooth power status.
        if (allowed) {
            const unsubscribe = Bluetooth.onStateChange(({status}) => {
                console.log({status})
                setState(status);
            })

            setName(Bluetooth.getName());
            setAddress(Bluetooth.getAddress())
            return () => {
                unsubscribe();
            }
        }
    }, [allowed])

    return <>
        <Card>
            <Container>
                <Text
                    style={{flex: 1}}>Bluetooth Adapter</Text>
            </Container>
            <OverlineText>{state === 'on' ? 'Bluetooth on device is on' : 'Bluetooth on device is off'}!</OverlineText>
            <OverlineText>
                Now discoverable as {name} and at address {address}
            </OverlineText>
        </Card>
    </>
};
