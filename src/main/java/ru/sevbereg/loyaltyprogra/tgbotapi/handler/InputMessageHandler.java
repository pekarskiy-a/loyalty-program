package ru.sevbereg.loyaltyprogra.tgbotapi.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;

public interface InputMessageHandler {

    SendMessage handle(Message message);

    BotState getHandlerName();
}
