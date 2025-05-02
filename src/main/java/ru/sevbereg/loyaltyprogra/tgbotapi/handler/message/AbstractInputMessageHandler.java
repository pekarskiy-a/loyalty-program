package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message;

import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputMessageHandler;

public abstract class AbstractInputMessageHandler implements InputMessageHandler {

    protected final UserBotStateService botStateService;

    protected final ReplyMessageService messageService;

    public AbstractInputMessageHandler(UserBotStateService botStateService, ReplyMessageService messageService) {
        this.botStateService = botStateService;
        this.messageService = messageService;
    }

}
