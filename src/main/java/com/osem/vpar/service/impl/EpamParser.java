package com.osem.vpar.service.impl;

import com.microsoft.playwright.Page;
import com.osem.vpar.model.Vacancy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("parser")
@Slf4j
public class EpamParser extends AbstractSiteParser {
    private static final String acceptCookies = "[id='onetrust-accept-btn-handler']";
    private static final String title = "div[data-testid='job-card-panel-container']";
    private static final String url = "a[data-testid='job-card-link']";

    @Value("${parser.epam.urls}")
    private List<String> urlsToScrape;

    private final VacancyFilterService vacancyFilterService;

    public EpamParser(VacancyFilterService vacancyFilterService) {
        this.vacancyFilterService = vacancyFilterService;
    }

    @Override
    protected void onPageLoad(Page page) {
        try {
            log.debug("Accept cookies.......");
            page.locator(acceptCookies).click();
        } catch (Exception e) {
            log.debug("Skip cookies.......");
        }
    }

    @Override
    public List<Vacancy> parse() {
        List<Vacancy> vacancies = new ArrayList<>();

        if (urlsToScrape == null || urlsToScrape.isEmpty()) {
            log.warn("⚠️ No URLs found in application.properties for careers.epam.com");
        }

        for (String url : urlsToScrape) {
            log.info("🔄 Processing URL from config: {}", url);

            List<Vacancy> found = super.scrapeCategory(url);

            vacancies.addAll(found);
        }
        return vacancies.stream().filter(vac -> vacancyFilterService.isTitleRelevant(vac.getTitle())).toList();
    }

    @Override
    protected List<Element> getVacancyElements(Document doc) {
        return doc.getElementsByAttributeValue("data-testid", "accordion-section-container"); //div[data-testid='accordion-section-container'
    }

    @Override
    protected String extractTitle(Element card) {
        Element el = card.selectFirst(title);
        return el != null ? el.text() : "No Title";
    }

    @Override
    protected String extractCompany(Element card) {
        Element el = card.selectXpath("//div[contains(@class, 'JobCard_workplaceWithLocation')]").first();
        return el != null ? "EPAM " + el.text() : "";
    }

    @Override
    protected String extractSalary(Element card) {
        return "Salary not specified ";
    }

    @Override
    protected String extractDate(Element card) {
        return "There is no Data published specified ";
    }

    @Override
    protected String extractUrl(Element card) {
        Element el = card.selectFirst(url);
        return el != null ? "https://careers.epam.com/" + el.attr("href") : "Link is not found";
    }


}
