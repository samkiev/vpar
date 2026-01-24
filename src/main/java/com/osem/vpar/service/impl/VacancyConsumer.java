package com.osem.vpar.service.impl;

import com.osem.vpar.bot.VacanciesBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Profile("bot")
@Slf4j
@RequiredArgsConstructor
public class VacancyConsumer {

    private final VacanciesBot vacanciesBot;

    @KafkaListener(topics = "vacancies-topic", groupId = "vacancy-bot-group")
    public void listen(String message) {
        log.info(" <== Received vacancy message from Kafka: {}", message);
        vacanciesBot.sendToAdmin(message);
    }
}
