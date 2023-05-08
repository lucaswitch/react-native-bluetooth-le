import {Bluetooth, BluetoothDevice} from "react-native-bluetooth-le";
import {useCallback, useEffect, useState} from "react";
import {
    FlatList,
    PermissionsAndroid,
    Platform,
    Text
} from "react-native";
import {Card} from "../../card";
import {Container, ListItem, ListItemInside} from "./style";
import {OverlineText} from '../../texts'
import {BondingButton} from "../bonding-button";

/**
 * Bluetooth bonding demo.
 * @constructor
 */
export function Bonding() {
    const [allowed, setAllowed] = useState(false);
    const [devices, setDevices] = useState<BluetoothDevice[]>([])
    const [enabled, setEnabled] = useState<boolean>(false)

    /**
     * Asks bluetooth permission.
     */
    const askBluetoothPermissions = useCallback(async () => {
        if (Platform.OS === 'android') {

            // Connect permission
            if (!await PermissionsAndroid.check('android.permission.BLUETOOTH_CONNECT'))
                await PermissionsAndroid.request('android.permission.BLUETOOTH_CONNECT', {
                    title: 'We need permission to access your bluetooth adapter.',
                    message: 'To proceed please press "Allow".',
                    buttonNegative: 'Cancel',
                    buttonPositive: 'Allow',
                })

            // Scan permission
            if (!await PermissionsAndroid.check('android.permission.BLUETOOTH_SCAN'))
                await PermissionsAndroid.request('android.permission.BLUETOOTH_SCAN', {
                    title: 'We need permission to access your bluetooth adapter scan.',
                    message: 'To proceed please press "Allow".',
                    buttonNegative: 'Cancel',
                    buttonPositive: 'Allow',
                })

            if (!await PermissionsAndroid.check('android.permission.ACCESS_FINE_LOCATION')) {
                await PermissionsAndroid.request('android.permission.ACCESS_FINE_LOCATION', {
                    title: 'We need permission to access your location to perform a bluetooth discovery.',
                    message: 'To proceed please press "Allow".',
                    buttonNegative: 'Cancel',
                    buttonPositive: 'Allow',
                })
            }
        }
    }, [])

    /**
     * Renders a device item.
     */
    const renderItem = useCallback((props: { index: number, item: BluetoothDevice }) => {
        const {address, name, rssi, dbm, bond} = props.item;

        console.log('bond', bond)
        return <ListItem key={props.index}>
            <ListItemInside>
                <OverlineText style={{color: '#000'}}>
                    {(name) ? name : ''}{(address ? `${address}` : '')}
                </OverlineText>
                <BondingButton address={address} bond={bond}/>
            </ListItemInside>
        </ListItem>
    }, [])

    useEffect(() => {
        askBluetoothPermissions().then(() => {
            const bondedDevices = Bluetooth.getBondedDevices();
            setDevices(bondedDevices);
        })
    }, [askBluetoothPermissions])


    return <Card>
        <Container>
            <Text>Bluetooth paired devices</Text>
            <OverlineText>- This is the current paired devices</OverlineText>
            <FlatList
                data={devices}
                renderItem={renderItem}
                scrollEnabled={true}
                style={{height: 200, marginTop: 10}}
            />
        </Container>
    </Card>

};
