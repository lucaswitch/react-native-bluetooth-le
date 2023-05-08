export type StatusChange = {
    status: "on" | "off" | "not_supported"
}

export type BluetoothDevice = {
    id: string | null,
    name: string | null,
    address: string,
    type: "le" | "dual" | "unknown",
    bond: "bonding" | "none",
    rssi: number,
    dbm: number
};
