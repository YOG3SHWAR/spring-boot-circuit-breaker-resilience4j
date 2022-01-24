package com.sapient.circuitbreakerdemo.exceptions;

public class RetryException extends Exception{
    public RetryException(String msg) {
        super(msg);
    }
}
