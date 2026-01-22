package com.osem.vpar.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VacancyConsumer {

    @KafkaListener(topics = "vacancies-topic", groupId = "vacancy-bot-group")
    public void listen(String message) {
        log.info(" <== Received vacancy message from Kafka: {}", message);
    }
}
