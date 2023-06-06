export type AdapterStatusEvent = {
    status: "on" | "off" | "not_supported"
}

export type BluetoothDevice = {
    id: string | null,
    name: string | null,
    address: string,
    type: BluetoothDeviceSupport,
    bond: BluetoothDeviceBondStatus,
    rssi: number,
    dbm: number
};

export type BluetoothDeviceService = {
    id: string
    type: "primary" | "secondary",
    characteristics: BluetoothDeviceCharacteristic[]
}

export type BluetoothDeviceCharacteristic = {
    id: string
    permissions: BluetoothDeviceCharacteristicPermissions,
    value: BluetoothDeviceCharacteristicValue
}

export type BluetoothDeviceCharacteristicValue = number[] | null;

export type BluetoothDeviceCharacteristicPermissions = "read" | "write" | "notify" | "broadcast" | "write_no_response";

export type BluetoothConnectionPriority = "LOW_POWER" | "HIGH" | "DCK" | "BALANCED";

export type BluetoothDeviceConnectionStatus = "connecting" | "connected" | "disconnecting" | "disconnected";

export type BluetoothDeviceBondStatus = "bonded" | "bonding" | "none";

export type BluetoothDeviceSupport = "le" | "dual" | "unknown";

export type BluetoothConnectionStatusEvent = {
    status: BluetoothDeviceConnectionStatus
}
