package com.osem.vpar.service.impl;

import com.microsoft.playwright.*;
import com.osem.vpar.model.Vacancy;
import com.osem.vpar.service.VacancyParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSiteParser implements VacancyParser {
    @Override
    public List<Vacancy> parse(String searchUrl) {
        List<Vacancy> vacancies = new ArrayList<>();

        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                     .setHeadless(false)
                     .setArgs(List.of("--start-maximized")))) {
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(null));
            Page page = context.newPage();

            System.out.println("Navigating to: " + searchUrl);
            page.navigate(searchUrl);
            //Check and accept cookie if needed
            onPageLoad(page);

            String htmlContent = page.content();
            Document doc = Jsoup.parse(htmlContent, searchUrl);
            List<Element> jsoupCards = getVacancyElements(doc);

            System.out.println("Items after expansion: " + jsoupCards.size());

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
            e.printStackTrace();
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
