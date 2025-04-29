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
public class AskPhoneNumberHandler extends AbstractInputMessageHandler {

    public AskPhoneNumberHandler(UserBotStateService botStateService, ReplyMessageService messageService) {
        super(botStateService, messageService);
    }

    @Override
    public SendMessage handle(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        botStateService.findByTgUserIdAndSaveState(userId, BotState.ENTER_PHONE_NUMBER);

        return messageService.getReplyMessageFromSource(chatId, "reply.askPhoneNumber"); //todo заменить на кнопку поделиться
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_PHONE_NUMBER;
    }

}
