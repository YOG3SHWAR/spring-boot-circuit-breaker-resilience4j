package com.sapient.circuitbreakerdemo.controller;

import com.sapient.circuitbreakerdemo.config.KafkaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    KafkaManager kafkaManager;

    @GetMapping(path = "/health")
    public String getHealth() {
        return "Up";
    }

    @GetMapping(path = "/resume")
    public void resumeConsumer() {
        kafkaManager.resume();
    }

    @GetMapping(path = "/pause")
    public void pauseConsumer() {
        kafkaManager.pause();
    }
}
