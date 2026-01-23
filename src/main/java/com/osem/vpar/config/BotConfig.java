package com.osem.vpar.config;

import com.osem.vpar.bot.VacanciesBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Profile("bot")
@Slf4j
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(VacanciesBot vacanciesBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        try {
            api.registerBot(vacanciesBot);
            log.info("---------------------------------------");
            log.info("✅ The Bot registered successfully: {}", vacanciesBot.getBotUsername());
            log.info("---------------------------------------");
        } catch (TelegramApiException e) {
            log.info("❌ Bot registration is failed  : {}", e.getMessage());
        }
        return api;
    }
}