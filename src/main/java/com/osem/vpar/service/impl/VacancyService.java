package com.osem.vpar.service.impl;

import com.osem.vpar.bot.VacanciesBot;
import com.osem.vpar.model.Vacancy;
import com.osem.vpar.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final VacanciesBot vacanciesBot;

    @Value("${bot.adminId}")
    private long adminChatId;

    public int saveNewVacancies(List<Vacancy> vacancies) {
        int count = 0;
        for (Vacancy vacancy : vacancies) {
            if (!vacancyRepository.existsByUrl(vacancy.getUrl())) {
                vacancyRepository.save(vacancy);

                String message = String.format(
                        "🔥 <b>%s</b>\n\n🏢 %s\n💰 %s\n📅 %s\n\n👉 <a href=\"%s\">Link</a>",
                        vacancy.getTitle(),
                        vacancy.getCompanyName(),
                        vacancy.getSalary(),
                        vacancy.getDateAdded(),
                        vacancy.getUrl()
                );

                vacanciesBot.sendMessage(adminChatId, message);

                count++;
            }
        }
        return count;
    }
}