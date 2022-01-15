package com.sapient.circuitbreakerdemo.listner;

import com.sapient.circuitbreakerdemo.service.Service;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Listner {

    @Autowired
    Service service;

    private final CircuitBreaker circuitBreaker;

    public Listner(final CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("fintech");
        this.circuitBreaker.getEventPublisher().onStateTransition(this::onStateChange);
    }

    @KafkaListener(topics = {"circuit_breaker_demo"})
    public void readMessage(String message) {
        log.info("message read from topic: {}", message);
        service.callRestApi(message);
    }

    private void onStateChange(final CircuitBreakerOnStateTransitionEvent event) {
        CircuitBreaker.State state = event.getStateTransition().getToState();
        log.error("hello yogeshwar");
        switch (state) {
            case OPEN:
            case CLOSED:
            case HALF_OPEN:
                log.info("circuit-breaker state is: {}", state);
                break;
        }
    }

}
