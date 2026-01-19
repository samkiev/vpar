package com.osem.vpar.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacancyFilterService {

    private static final List<String> AUTOMATION_MARKERS = List.of(
            "automation", "automated", "automating", "autoqa", "aqa",
            "selenium", "playwright", "rest assured", "sdet", "qa", "test", "testing",
            "junit", "testng"
    );

    private static final List<String> QA_TITLES = List.of(
            "qa", "tester", "test engineer", "quality assurance", "software engineer in test", "test"
    );

    private static final String LANGUAGE = "java";

    public boolean isTitleRelevant(String title) {
        if (title == null || title.isBlank()) {
            return false;
        }

        String lowerTitle = title.toLowerCase();

        boolean hasAutomationMarker = AUTOMATION_MARKERS.stream()
                .anyMatch(lowerTitle::contains);

        if (hasAutomationMarker) return true;

        boolean hasQaTitle = QA_TITLES.stream().anyMatch(lowerTitle::contains);
        boolean hasJava = lowerTitle.contains(LANGUAGE);

        return hasQaTitle && hasJava;
    }
}