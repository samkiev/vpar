package com.osem.vpar.service.impl;

import com.osem.vpar.model.Vacancy;
import com.osem.vpar.model.dto.JoobleDto;
import com.osem.vpar.model.dto.JoobleJobRequestDto;
import com.osem.vpar.model.dto.JoobleResponseDto;
import com.osem.vpar.service.VacancyParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("parser")
@Slf4j
public class JoobleApiParser implements VacancyParser {
    private final VacancyFilterService vacancyFilterService;
    private final RestClient restClient;
    @Value("${jooble.api.key}")
    private String apiKey;
    @Value("${parser.jooble.url}")
    private String searchUrl;
    @Value("${parser.jooble.keywords}")
    List<String> keyWords;

    public JoobleApiParser(VacancyFilterService vacancyFilterService) {
        this.vacancyFilterService = vacancyFilterService;
        this.restClient = RestClient.create();
    }


    @Override
    public List<Vacancy> parse() {
        log.info("🚀 Starting Jooble parser for url: {}", searchUrl);
        List<Vacancy> vacancies = new ArrayList<>();
        for (String keyWord : keyWords) {
            try {
                JoobleJobRequestDto requestDto = JoobleJobRequestDto.builder()
                        .keywords(keyWord)
                        .location("Poland")
                        .build();
                JoobleResponseDto responseDto = restClient.post()
                        .uri(searchUrl + apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestDto)
                        .retrieve()
                        .body(JoobleResponseDto.class);

                if (responseDto != null && responseDto.getJobs() != null) {
                    responseDto.getJobs().stream().filter(job -> vacancyFilterService.isTitleRelevant(job.getTitle()))
                            .map(this::mapToEntity)
                            .forEach(vacancies::add);
                    log.info("🚀 Jooble parsing for keyword: {}", keyWord);
                }
            } catch (Exception e) {
                log.error("❌ Error during Jooble parsing", e);
            }
        }

        log.info("✅ Jooble parsing finished. Found {} vacancies", vacancies.size());
        return vacancies;
    }

    private Vacancy mapToEntity(JoobleDto job) {
        return Vacancy.builder()
                .title(job.getTitle())
                .salary(formatSalary(job.getSalary()))
                .companyName(job.getCompany())
                .url(job.getUrl())
                .dateAdded(formatDate(job.getDateAdded()))
                .build();
    }
    private String formatSalary(String salary) {
        return (salary != null && !salary.isBlank()) ? salary : "Salary not specified";
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.length() < 10) {
            return LocalDate.now().toString();
        }
        return dateStr.substring(0, 10);
    }

}
