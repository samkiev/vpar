package com.osem.vpar;

import com.osem.vpar.service.VacancyParser;
import com.osem.vpar.service.impl.PracujPlParcer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // 1. Говорим Спрингу: "Это тоже твой бин, управляй им"
@RequiredArgsConstructor // 2. LOMBOK: Генерирует конструктор для final полей
public class ConsoleRunner implements CommandLineRunner {

    // 3. Мы объявляем интерфейс, а не реализацию! (DIP)
    private final PracujPlParcer vacancyParser;

    // Спринг видит конструктор (от Lombok) и понимает:
    // "Ага, этому классу нужен VacancyParser. У меня в коробке как раз лежит MockVacancyParser.
    // Дай-ка я его сюда подставлю". Это и есть Dependency Injection.

    @Override
    public void run(String... args) throws Exception {
        System.out.println("---------------------------------------");
        System.out.println("🚀 STARTING PARSER...");

        var vacancies = vacancyParser.parse("https://it.pracuj.pl/praca?et=18%2C4&tc=0%2C3&its=testing&itth=38");

        vacancies.forEach(v -> {
            System.out.println("Found: " + v.getTitle() + " at " + v.getCompanyName() + " With Salary: "
                    + v.getSalary() + " Date published: " + v.getDateAdded() + " Vacancy Url: " + v.getUrl());
        });

        System.out.println("---------------------------------------");
    }
}