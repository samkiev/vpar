package com.osem.vpar.config;

import com.osem.vpar.bot.VacanciesBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(VacanciesBot vacanciesBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        try {
            api.registerBot(vacanciesBot);
            System.out.println("---------------------------------------");
            System.out.println("✅ The Bot registered successfully: " + vacanciesBot.getBotUsername());
            System.out.println("---------------------------------------");
        } catch (TelegramApiException e) {
            System.err.println("❌ Bot registration is failed  : " + e.getMessage());
        }
        return api;
    }
}