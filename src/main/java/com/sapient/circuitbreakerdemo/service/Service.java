package com.sapient.circuitbreakerdemo.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
@Slf4j
public class Service {

    @CircuitBreaker(name = "fintech", fallbackMethod = "fallBack")
//    @CircuitBreaker(name = "fintech")
//    @Retry(name = "fintech")
    public void callRestApi() {
        log.info("yogeshwar retrying");
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
//        final String uri = "http://localhost:1080/circuit-breaker";
//        log.info("{}", uri);
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject(uri, String.class);
//        log.info("response = {}", response);
    }

    public void fallBack(Exception e) {
        log.info("fallback part is working");
    }
}
