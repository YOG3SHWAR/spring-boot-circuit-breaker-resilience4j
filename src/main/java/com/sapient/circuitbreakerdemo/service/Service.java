package com.sapient.circuitbreakerdemo.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
@Slf4j
public class Service {

    @Value(value = "${baseUrl}")
    private String baseUrl;

//    @CircuitBreaker(name = "fintech", fallbackMethod = "fallBack")
    @CircuitBreaker(name = "fintech")
    @Retry(name = "fintech")
    public void callRestApi(String message) {
        log.info("calling rest api...");
        callMockserver();
    }

    private void callMockserver() {
        log.info("calling mockserver...");
        String uri = baseUrl + "circuit-breaker";
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        String response = restTemplate.getForObject(uri, String.class);
        log.info("response = {}", response);
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
