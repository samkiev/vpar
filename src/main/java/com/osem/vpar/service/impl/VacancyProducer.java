package com.osem.vpar.service.impl;

import com.osem.vpar.model.Vacancy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class VacancyProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "vacancies-topic";

    public void sendVacancy(Vacancy vacancy) {
        log.info("==> Sending vacancy message to Kafka: {}", vacancy.getTitle());

        String message =  "VACANCY: " + vacancy.getTitle() + " | " + vacancy.getUrl();
        kafkaTemplate.send(TOPIC, message);
    }
}
