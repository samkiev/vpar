package com.osem.vpar.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("bot")
@RequiredArgsConstructor
@Slf4j
public class CommandProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendScanCommand() {
        kafkaTemplate.send("command-topic", "START_SCAN");
    }
}
