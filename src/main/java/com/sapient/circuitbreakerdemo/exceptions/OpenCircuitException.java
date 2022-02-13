package com.sapient.circuitbreakerdemo.exceptions;

import java.io.IOException;

public class OpenCircuitException extends IOException {
    public OpenCircuitException(String msg) {
        super(msg);
    }
}
