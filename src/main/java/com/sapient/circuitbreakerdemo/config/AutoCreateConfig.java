package com.sapient.circuitbreakerdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
@Profile("local") // this configuration will only run in local profile
public class AutoCreateConfig {
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("${spring.kafka.consumer.topic}").partitions(1).replicas(1).build();
    }
}
