package com.osem.vpar.service.impl;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.osem.vpar.model.Vacancy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LuxSoftParser extends AbstractSiteParser {
    private static final String dropDownPagesElement = "//div[@class='container jobs']//ul[@class='dropdown-menu show']";
    private static final String acceptCookiesButton = "[id='onetrust-accept-btn-handler']";
    private static final String dateOfPublishedElement = "//img[@alt='date icon']/following-sibling::p";
    private static final String titleElement = "[class='subtitle-l text-rich-black']";

    private static final String luxoftPrefix = "https://career.luxoft.com";

    @Value("${parser.luxoft.urls}")
    private List<String> urlsToScrape;

    @Override
    protected void onPageLoad(Page page) {
        try {
            log.debug("Accept cookies.......");
            page.locator(acceptCookiesButton).click();
        } catch (Exception e) {
            log.debug("Skip cookies.......");
        }
        page.locator("[class='jobs__listing-summary__options']").click();
        page.locator(dropDownPagesElement)
                .getByRole(AriaRole.LISTITEM)
                .filter(new Locator.FilterOptions().setHasText("60"))
                .click();
    }

    @Override
    protected List<Element> getVacancyElements(Document doc) {
        return doc.getElementsByAttributeValue("class", "jobs__list__job");
    }

    @Override
    protected String extractTitle(Element card) {
        Element el = card.selectFirst(titleElement);
        return el != null ? el.text() : "Title not found";
    }

    @Override
    protected String extractCompany(Element card) {
        return "LuxSoft";
    }

    @Override
    protected String extractSalary(Element card) {
        return "Salary is not specified";
    }

    @Override
    protected String extractUrl(Element card) {
        return card != null ? luxoftPrefix + card.attr("href") : "Link is not found";
    }

    @Override
    protected String extractDate(Element card) {
        try {
            Document doc = Jsoup.connect(extractUrl(card)).get();
            Element el = doc.selectXpath(dateOfPublishedElement).first();
            return el != null ? el.text() : "Date is not found";
        } catch (IOException e) {
            return "Date is not found";
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
        return vacancies;
    }
}
