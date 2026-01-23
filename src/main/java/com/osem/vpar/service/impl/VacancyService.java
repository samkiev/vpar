package com.osem.vpar.service.impl;

import com.osem.vpar.model.Vacancy;
import com.osem.vpar.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VacancyService {

    private final VacancyRepository vacancyRepository;

    @Transactional
    public boolean trySave(Vacancy vacancy) {
        if (vacancyRepository.existsByUrl(vacancy.getUrl())) {
            return false;
        }
        vacancyRepository.save(vacancy);
        return true;
    }
}
