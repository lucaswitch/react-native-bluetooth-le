package com.rnbluetoothle.bluetooth.bridge.errors;

/**
 * Represents the js discovery error.
 */
public class JsDiscoveryError extends JsError {

    public enum ErrorCodes {
        GATT_FAILURE,
        GATT_CONNECTION_CONGESTED,
        GATT_INSUFFICIENT_AUTHENTICATION,
        GATT_INSUFFICIENT_ENCRYPTION,
        GATT_REQUEST_NOT_SUPPORTED
    }

    JsDiscoveryError(int code, String message) {
        super(code, message);
    }
}
