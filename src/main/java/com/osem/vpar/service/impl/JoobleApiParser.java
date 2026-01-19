package com.osem.vpar.service.impl;

import com.osem.vpar.model.Vacancy;
import com.osem.vpar.model.dto.JoobleJobRequestDto;
import com.osem.vpar.model.dto.JoobleResponseDto;
import com.osem.vpar.service.VacancyParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class JoobleApiParser implements VacancyParser {
    private final VacancyFilterService vacancyFilterService;
    private final RestClient restClient;
    @Value("${jooble.api.key}")
    private String apiKey;

    public JoobleApiParser(VacancyFilterService vacancyFilterService) {
        this.vacancyFilterService = vacancyFilterService;
        this.restClient = RestClient.create();
    }

    @Override
    public List<Vacancy> parse(String searchUrl) {
        log.info("🚀 Starting Jooble parser for url: {}", searchUrl);
        List<Vacancy> vacancies = new ArrayList<>();
        try {
            JoobleJobRequestDto requestDto = JoobleJobRequestDto.builder()
                    .keywords("Automated Test Engineer, java")
                    .location("Poland")
                    .build();
            JoobleResponseDto responseDto = restClient.post()
                    .uri(searchUrl + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestDto)
                    .retrieve()
                    .body(JoobleResponseDto.class);

            if (responseDto != null && responseDto.getJobs() != null) {
                responseDto.getJobs().stream().filter(job -> vacancyFilterService.isTitleRelevant(job.getTitle())).
                        forEach(job -> {
                            vacancies.add(Vacancy.builder()
                                    .title(job.getTitle())
                                    .salary(job.getSalary() != null ? job.getSalary() : "Salary is not specified.")
                                    .companyName(job.getCompany())
                                    .url(job.getUrl())
                                    .dateAdded(job.getDateAdded() != null ? job.getDateAdded().substring(0, 10) : LocalDate.now().toString())
                                    .build()
                            );
                        });
            }
        } catch (Exception e) {
            log.error("❌ Error during Jooble parsing", e);
        }


        log.info("✅ Jooble parsing finished. Found {} vacancies", vacancies.size());
        return vacancies;
    }
}
