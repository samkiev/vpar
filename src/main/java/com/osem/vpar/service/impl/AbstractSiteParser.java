package com.osem.vpar.service.impl;

import com.microsoft.playwright.*;
import com.osem.vpar.model.Vacancy;
import com.osem.vpar.service.VacancyParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public abstract class AbstractSiteParser implements VacancyParser {

    public List<Vacancy> scrapeCategory(String searchUrl){
        List<Vacancy> vacancies = new ArrayList<>();

        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                     .setHeadless(false)
                     .setArgs(List.of("--start-maximized")))) {
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(null));
            Page page = context.newPage();

            log.info("Navigating to: {}", searchUrl);
            page.navigate(searchUrl);
            //Check and accept cookie if needed
            onPageLoad(page);

            String htmlContent = page.content();
            Document doc = Jsoup.parse(htmlContent, searchUrl);
            List<Element> jsoupCards = getVacancyElements(doc);

            log.info("Items after expansion: {}", jsoupCards.size());

            try {
                for (Element card : jsoupCards) {
                    vacancies.add(Vacancy.builder()
                            .title(extractTitle(card))
                            .companyName(extractCompany(card))
                            .salary(extractSalary(card))
                            .dateAdded(extractDate(card))
                            .url(extractUrl(card))
                            .build());
                }
            } catch (Exception e) {
                System.err.println("Error parsing card: " + e.getMessage());
            }

        } catch (Exception e) {
            log.error("Error parsing card: {}", e.getMessage());
        }

        return vacancies;
    }

    protected abstract void onPageLoad(Page page);

    protected abstract List<Element> getVacancyElements(Document doc);

    protected abstract String extractTitle(Element card);

    protected abstract String extractCompany(Element card);

    protected abstract String extractSalary(Element card);

    protected abstract String extractUrl(Element card);

    protected abstract String extractDate(Element card);
}
