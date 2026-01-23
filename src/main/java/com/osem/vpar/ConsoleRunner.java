package com.osem.vpar;

import com.osem.vpar.model.Vacancy;
import com.osem.vpar.service.VacancyParser;
import com.osem.vpar.service.impl.VacancyProducer;
import com.osem.vpar.service.impl.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("parser")
@Slf4j
public class ConsoleRunner implements CommandLineRunner {

    private final List<VacancyParser> parsers;
    private final VacancyService vacancyService;
    private final VacancyProducer vacancyProducer;

    @Override
    public void run(String... args) {

        List<Vacancy> vacancies = new ArrayList<>();
        log.info("---------------------------------------");
        log.info("🚀 STARTING PARSER...");

        for (VacancyParser parser : parsers) {
            String parserName = parser.getClass().getSimpleName();
            log.info("▶ Running parser: {}", parserName);
            try {
                var found = parser.parse();
                vacancies.addAll(found);
                log.info("✔ {} finished. Accumulated {} vacancies so far.", parserName, vacancies.size());
            } catch (Exception e) {
                log.error("❌ Parser {} failed", parserName, e);
            }

        }

        vacancies.forEach(v -> log.info("Found: {} at {} With Salary: {} Date published: {} Vacancy Url: {}", v.getTitle(), v.getCompanyName(), v.getSalary(), v.getDateAdded(), v.getUrl()));

        var filteredVacancy = vacancies.stream().filter(vacancyService::trySave).toList();
        filteredVacancy.forEach(vacancyProducer::sendVacancy);

        log.info("---------------------------------------");
        log.info("Parsing finished. Found: {}. New Saved: {}", vacancies.size(), filteredVacancy.size());
        log.info("---------------------------------------");

    }

}