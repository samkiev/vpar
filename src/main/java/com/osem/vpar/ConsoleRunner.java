package com.osem.vpar;

import com.osem.vpar.model.Vacancy;
import com.osem.vpar.service.VacancyParser;
import com.osem.vpar.service.impl.JoobleApiParser;
import com.osem.vpar.service.impl.PracujPlParser;
import com.osem.vpar.service.impl.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsoleRunner implements CommandLineRunner {

    private final List<VacancyParser> parsers;
    private final VacancyService vacancyService;

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

        vacancies.forEach(v -> {
            log.info("Found: {} at {} With Salary: {} Date published: {} Vacancy Url: {}", v.getTitle(), v.getCompanyName(), v.getSalary(), v.getDateAdded(), v.getUrl());
        });


        int savedCount = vacancyService.saveNewVacancies(vacancies);

        log.info("---------------------------------------");
        log.info("Job done! Found: {}. New Saved: {}", vacancies.size(), savedCount);
        log.info("---------------------------------------");
    }
}