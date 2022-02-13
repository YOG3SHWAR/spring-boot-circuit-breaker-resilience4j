package com.sapient.circuitbreakerdemo.service;

import com.sapient.circuitbreakerdemo.errorhandler.RestTemplateResponseErrorHandler;
import com.sapient.circuitbreakerdemo.exceptions.OpenCircuitException;
import com.sapient.circuitbreakerdemo.exceptions.RetryException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
@Slf4j
public class Service {

    @Value(value = "${baseUrl}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public Service(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .requestFactory(this::getClientHttpRequestFactory)
                .build();
    }

    //    @CircuitBreaker(name = "fintech", fallbackMethod = "fallBack")
    @CircuitBreaker(name = "fintech")
    @Retry(name = "fintech")
    public void callRestApi(String message) throws RetryException, OpenCircuitException {
        log.info("calling rest api...");
        callMockserver();
    }

    private void callMockserver()
            throws RetryException, OpenCircuitException {
        log.info("calling mockserver...");
        String uri = baseUrl + "circuit-breaker";
//        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory())
//        String response = restTemplate.getForObject(uri, String.class)
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
//        if (response.getStatusCode() != HttpStatus.OK) {
//            throwException(response.getStatusCode());
//        }
        log.info("response = {}", response.getBody());
    }

    private void throwException(HttpStatus statusCode)
            throws RetryException, OpenCircuitException {
        log.info("status code is: {}", statusCode);
        if (statusCode == HttpStatus.SERVICE_UNAVAILABLE)
            throw new RetryException("SERVICE_UNAVAILABLE: retrying");
        else throw new OpenCircuitException(statusCode + ", circuit opening exception");
    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory
                = new SimpleClientHttpRequestFactory();
        //Connect timeout
        clientHttpRequestFactory.setConnectTimeout(5000);

        //Read timeout
        clientHttpRequestFactory.setReadTimeout(5000);
        return clientHttpRequestFactory;
    }

    public void fallBack(Exception e) {
        log.info("fallback method with exception: {}", e.getMessage());
    }
}
