package com.osem.vpar.service.impl;

import com.microsoft.playwright.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("pracparser")
@Slf4j
public class PracujPlParser extends AbstractSiteParser {
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
            log.debug("Skip cookies.......");
        }
        try {
            List<Locator> elements = page.locator("div[data-test='default-offer']").all();
            elements.stream().filter(el -> !el.locator(vacancyLink).isVisible())
                    .forEach(el -> el.locator(title).click());
            page.waitForTimeout(500);
        } catch (Exception e) {
            log.error("Error during expansion: {}", e.getMessage());
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
        String rawUrl = "Link is not found";

        if (!links.isEmpty()) {
            if (links.size() == 1) {
                rawUrl = links.first().absUrl("href");
            } else {
                Element krakowLink = card.selectFirst(vacancyLink + ":contains(Kraków)");
                if (krakowLink != null) {
                    rawUrl = krakowLink.absUrl("href");
                } else {
                    rawUrl = links.first().absUrl("href");
                }
            }
        }

        return sanitizeUrl(rawUrl);
    }

    String sanitizeUrl(String url) {
        if (url == null) {
            return "Link is not found: ";
        }
        int index = url.indexOf("?");
        if (index != -1) {
            return url.substring(0, index);
        }
        return url;
    }

}

