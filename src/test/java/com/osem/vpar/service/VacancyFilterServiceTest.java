package com.osem.vpar.service;

import com.osem.vpar.service.impl.VacancyFilterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class VacancyFilterServiceTest {

    private final VacancyFilterService filterService = new VacancyFilterService();

    @ParameterizedTest
    @CsvSource({
            "Senior Automation QA Engineer, true",
            "Java SDET, true",
            "Software Tester (Java), true",
            "QA Engineer with Java, true",
            "Manual QA Engineer, true",
            "Java Developer, false",
            "Python Automation Engineer, true",
            " , false",
            "null, false"
    })
    void testIsTitleRelevant(String title, String expectedResultStr) {
        boolean expected = Boolean.parseBoolean(expectedResultStr);
        boolean actual = filterService.isTitleRelevant(title);

        Assertions.assertEquals(expected, actual, "Failed for title: " + title);
    }
}