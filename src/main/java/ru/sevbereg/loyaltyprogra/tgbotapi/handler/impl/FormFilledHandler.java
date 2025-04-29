package ru.sevbereg.loyaltyprogra.tgbotapi.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;

@Slf4j
@Component
public class FormFilledHandler extends AbstractInputMessageHandler {

    public FormFilledHandler(UserBotStateService botStateService, ReplyMessageService messageService) {
        super(botStateService, messageService);
    }

    @Override
    public SendMessage handle(Message message) {
        return messageService.getReplyMessageFromSource(message.getChatId(), "reply.clientCreated");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FORM_FILLED;
    }
}
