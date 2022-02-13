package com.sapient.circuitbreakerdemo.config;

import com.sapient.circuitbreakerdemo.exceptions.RetryException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RetryCustomConfig {
    @Bean
    public void retryConfig() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(1000))
                .retryOnException(e -> e instanceof RetryException)
                .failAfterMaxAttempts(false)
                .build();

// Create a RetryRegistry with a custom global configuration
        RetryRegistry registry = RetryRegistry.of(config);

// Get or create a Retry from the registry -
// Retry will be backed by the default config
        Retry retryWithDefaultConfig = registry.retry("name1");

// Get or create a Retry from the registry,
// use a custom configuration when creating the retry
//        RetryConfig custom = RetryConfig.custom()
//                .waitDuration(Duration.ofMillis(100))
//                .build();

//        Retry retryWithCustomConfig = registry.retry("name2", config);
    }
}
