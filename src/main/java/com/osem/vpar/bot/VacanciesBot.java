package com.osem.vpar.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class VacanciesBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    public VacanciesBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("\uD83D\uDCE9 Update resieved: {}", update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            log.info("\uD83D\uDCAC Message from {}: {}", chatId, text);

            String response = "Hi! Your id is: " + chatId;
            sendMessage(chatId, response);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setParseMode("HTML");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Something went wrong: {}", e.getMessage());

        }
    }
}