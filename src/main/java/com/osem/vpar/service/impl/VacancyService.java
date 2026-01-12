package com.osem.vpar.service.impl;

import com.osem.vpar.model.Vacancy;
import com.osem.vpar.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyService {

    private final VacancyRepository vacancyRepository;

    public int saveNewVacancies(List<Vacancy> vacancies) {
        int count = 0;
        for (Vacancy vacancy : vacancies) {
            if (!vacancyRepository.existsByUrl(vacancy.getUrl())) {
                vacancyRepository.save(vacancy);
                count++;
            }
        }
        return count;
    }
}