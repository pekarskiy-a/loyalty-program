package ru.sevbereg.loyaltyprogra.tgbotapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.sevbereg.loyaltyprogra.util.JsonUtils;

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

        this.sendMessage(message);
    }

    public void sendMessage(SendMessage message) {
        try {
            log.info("EMPLOYEE. Отправка сообщения клиенту с chatId: {}", message.getChatId());
            log.debug("EMPLOYEE. Сообщение {}{}", System.lineSeparator(), JsonUtils.toJson(message));
            execute(message);
        } catch (TelegramApiException e) {
            log.error("EMPLOYEE. Ошибка отправки сообщения", e);
        }
    }

}
