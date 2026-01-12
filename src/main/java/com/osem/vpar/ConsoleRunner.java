package com.osem.vpar;

import com.osem.vpar.service.impl.PracujPlParser;
import com.osem.vpar.service.impl.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsoleRunner implements CommandLineRunner {

    private final PracujPlParser vacancyParser;
//    private final VacancyRepository vacancyRepository;
    private final VacancyService vacancyService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("---------------------------------------");
        System.out.println("🚀 STARTING PARSER...");

        var vacancies = vacancyParser.parse("https://it.pracuj.pl/praca?et=18%2C4&tc=0%2C3&its=testing&itth=38");

        vacancies.forEach(v -> {
            System.out.println("Found: " + v.getTitle() + " at " + v.getCompanyName() + " With Salary: "
                    + v.getSalary() + " Date published: " + v.getDateAdded() + " Vacancy Url: " + v.getUrl());
        });


        int savedCount = vacancyService.saveNewVacancies(vacancies);

        System.out.println("---------------------------------------");
        System.out.println("Job done! Found: " + vacancies.size() + ". New Saved: " + savedCount);
        System.out.println("---------------------------------------");
    }
}