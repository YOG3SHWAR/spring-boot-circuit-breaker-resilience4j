package com.sapient.circuitbreakerdemo.exceptions;

public class CircuitOpenException extends Exception {
    public CircuitOpenException(String msg) {
        super(msg);
    }
}
