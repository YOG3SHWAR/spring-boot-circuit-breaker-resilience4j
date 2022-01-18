package com.sapient.circuitbreakerdemo.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class Producer {

    @Value(value = "${spring.kafka.consumer.topic}")
    String topic;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    private ListenableFuture<SendResult<String, String>> sendMessage(String message)
            throws InterruptedException, ExecutionException {

        ListenableFuture<SendResult<String, String>> listenableFuture = null;
        listenableFuture = kafkaTemplate.send(topic, message);
        return listenableFuture;
    }

    public void send(String message) {
        ListenableFuture<SendResult<String, String>> resp = null;
        try {
            log.info("posting message to topic: {}", message);
            resp = this.sendMessage(message);
            log.info("message sent to topic, resp; {}", resp);
        } catch (InterruptedException | ExecutionException e) {
            log.error(
                    "InterruptedException | ExecutionException Error sending the message, value = {}, message = {}",
                    message, e.getMessage());
        }
    }
}
