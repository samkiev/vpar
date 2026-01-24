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
        String safeUrl = vacancy.getUrl().replace("&", "&amp;");
        String message = String.format(
                "🔥 <b>%s</b>\n🏢 %s\n💰 %s\n🗓 %s\n👉 <a href=\"%s\">Link</a>",
                vacancy.getTitle(),
                vacancy.getCompanyName(),
                vacancy.getSalary(),
                vacancy.getDateAdded(),
                safeUrl
        );

        log.info("📤 Sending vacancy to Kafka: {}", vacancy.getTitle());
        kafkaTemplate.send(TOPIC, message);
    }
}
