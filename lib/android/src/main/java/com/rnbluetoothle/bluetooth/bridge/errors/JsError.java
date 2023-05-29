package com.rnbluetoothle.bluetooth.bridge.errors;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

/**
 * Represents the js error.
 */
public class JsError {
    protected String code;
    protected String message;

    JsError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets the error code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the Js error object.
     */
    public WritableMap getMap() {
        WritableMap jsError = Arguments.createMap();
        jsError.putString("message", this.getMessage());
        jsError.putString("code", this.getCode());

        return jsError;
    }
}
