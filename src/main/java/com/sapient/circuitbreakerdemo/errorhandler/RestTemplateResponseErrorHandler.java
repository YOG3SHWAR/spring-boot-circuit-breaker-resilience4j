package com.sapient.circuitbreakerdemo.errorhandler;

import com.sapient.circuitbreakerdemo.exceptions.OpenCircuitException;
import com.sapient.circuitbreakerdemo.exceptions.RetryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Component
@Slf4j
public class RestTemplateResponseErrorHandler
        implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {
        return httpResponse.getStatusCode() != OK;
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        log.info("request unsuccessful, status {}: ", httpResponse.getStatusCode());
        if (httpResponse.getStatusCode() == SERVICE_UNAVAILABLE) {
            log.info("throwing RetryException");
            throw new RetryException("retrying");
        } else {
            log.info("throwing CircuitOpenException");
            throw new OpenCircuitException("opening circuit");
        }
    }
}
