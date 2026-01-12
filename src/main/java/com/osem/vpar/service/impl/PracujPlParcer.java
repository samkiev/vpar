package com.osem.vpar.service.impl;

import com.microsoft.playwright.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PracujPlParcer extends AbstractSiteParser {
    private static final String PRACUJ_BASEURL = "https://it.pracuj.pl/praca?et=18%2C4&tc=0%2C3&its=testing&itth=38";
    //locators
    private static final String title = "h2[data-test='offer-title']";
    private static final String company = "h3[data-test='text-company-name']";
    private static final String salary = "span[data-test='offer-salary']";
    private static final String vacancyLink = "a[data-test='link-offer']";
    private static final String dateAdded = "p[data-test='text-added']";

    @Override
    protected void onPageLoad(Page page) {
        try {
            page.locator("//button[@data-test='button-submitCookie']").click();
        } catch (Exception e) {

        }
        try {
            List<Locator> elements = page.locator("div[data-test='default-offer']").all();
            elements.stream().filter(el -> !el.locator(vacancyLink).isVisible())
                    .forEach(el -> el.locator(title).click());
            page.waitForTimeout(500);
        } catch (Exception e) {
            System.err.println("Error during expansion: " + e.getMessage());
        }
    }

    @Override
    protected List<Element> getVacancyElements(Document doc) {
        return doc.getElementsByAttributeValue("data-test", "default-offer");
    }

    @Override
    protected String extractTitle(Element card) {
        Element el = card.selectFirst(title);
        return el != null ? el.text() : "No Title";
    }

    @Override
    protected String extractCompany(Element card) {
        Element el = card.selectFirst(company);
        return el != null ? el.text() : "";
    }

    @Override
    protected String extractSalary(Element card) {
        Element el = card.selectFirst(salary);
        return el != null ? el.text() : "Salary not specified ";
    }

    @Override
    protected String extractDate(Element card) {
        Element el = card.selectFirst(dateAdded);
        return el != null ? el.text() : "There is no Data published specified ";
    }

    @Override
    protected String extractUrl(Element card) {
        Elements links = card.select(vacancyLink);
        if (links.isEmpty())
            return "Link is not found";
        if (links.size() == 1) {
            return links.getFirst().absUrl("href");
        }

        Element krakowLink = card.selectFirst(vacancyLink + ":contains(Kraków)");
        if (krakowLink != null) {
            return krakowLink.absUrl("href");
        }
        return links.first().absUrl("href");
    }

}

