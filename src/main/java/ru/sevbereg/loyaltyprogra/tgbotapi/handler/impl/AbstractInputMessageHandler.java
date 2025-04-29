package ru.sevbereg.loyaltyprogra.tgbotapi.handler.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputMessageHandler;

public class AbstractInputMessageHandler implements InputMessageHandler {

    protected final UserBotStateService botStateService;

    protected final ReplyMessageService messageService;

    public AbstractInputMessageHandler(UserBotStateService botStateService, ReplyMessageService messageService) {
        this.botStateService = botStateService;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return null;
    }

}
