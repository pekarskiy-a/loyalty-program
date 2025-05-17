package ru.sevbereg.loyaltyprogra.tgbotapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramSender extends DefaultAbsSender {

    protected TelegramSender(@Value("${telegram.bot.token}") String botToken) {
        super(new DefaultBotOptions(), botToken);
    }

    public void sendMessage(Long chatId, String textMessage) {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(textMessage)
                .build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения ", e);
        }
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения ", e);
        }
    }

}
