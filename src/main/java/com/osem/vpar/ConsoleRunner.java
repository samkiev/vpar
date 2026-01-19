package com.osem.vpar;

import com.osem.vpar.model.Vacancy;
import com.osem.vpar.service.impl.JoobleApiParser;
import com.osem.vpar.service.impl.PracujPlParser;
import com.osem.vpar.service.impl.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsoleRunner implements CommandLineRunner {

    private final PracujPlParser pracujPlParser;
    private final VacancyService vacancyService;
    private final JoobleApiParser joobleApiParser;

    @Override
    public void run(String... args) {
        System.out.println("---------------------------------------");
        log.info("🚀 STARTING PARSER...");

        List<Vacancy> vacancies = pracujPlParser.parse("https://it.pracuj.pl/praca?et=18%2C4&tc=0%2C3&its=testing&itth=38");

        List<Vacancy> joobleVacancy = joobleApiParser.parse("https://jooble.org/api/");

        vacancies.addAll(joobleVacancy);
        vacancies.forEach(v -> {
            log.info("Found: {} at {} With Salary: {} Date published: {} Vacancy Url: {}", v.getTitle(), v.getCompanyName(), v.getSalary(), v.getDateAdded(), v.getUrl());
        });


        int savedCount = vacancyService.saveNewVacancies(vacancies);

        System.out.println("---------------------------------------");
        log.info("Job done! Found: {}. New Saved: {}", vacancies.size(), savedCount);
        System.out.println("---------------------------------------");
    }
}