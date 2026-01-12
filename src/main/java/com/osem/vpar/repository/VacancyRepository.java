package com.osem.vpar.repository;

import com.osem.vpar.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    boolean existsByUrl(String url);
}
