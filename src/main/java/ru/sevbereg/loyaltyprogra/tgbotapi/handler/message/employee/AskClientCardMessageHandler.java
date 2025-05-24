package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message.employee;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputMessageHandler;

import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.ASK_CLIENT_CARD;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskClientCardMessageHandler implements InputMessageHandler {

    protected final UserBotStateService botStateService;
    protected final ReplyMessageService messageService;

    @Override
    public SendMessage handle(Message message) {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();
        log.trace("EMPLOYEE. Обработчик запроса номера клиента");
        botStateService.updateEmployeeState(tgUserId, BotState.ENTER_PHONE_OR_CARD_ID, null);
        return messageService.getReplyMessageFromSource(chatId, "replay.employee.enter.phoneOrCardNumber");
    }

    @Override
    public BotState getHandlerName() {
        return ASK_CLIENT_CARD;
    }
}
