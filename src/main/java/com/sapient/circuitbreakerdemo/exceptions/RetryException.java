package com.sapient.circuitbreakerdemo.exceptions;

import java.io.IOException;

public class RetryException extends IOException{
    public RetryException(String msg) {
        super(msg);
    }
}
