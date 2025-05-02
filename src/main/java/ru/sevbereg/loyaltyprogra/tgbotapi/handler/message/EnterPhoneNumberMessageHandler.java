package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.facade.tgbot.ClientTgBotFacade;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.util.PhoneFormatterUtils;

import java.util.Objects;

@Slf4j
@Component
public class EnterPhoneNumberMessageHandler extends AbstractInputMessageHandler {

    private final ClientTgBotFacade clientFacade;

    public EnterPhoneNumberMessageHandler(UserBotStateService botStateService,
                                          ReplyMessageService messageService,
                                          ClientTgBotFacade clientFacade) {
        super(botStateService, messageService);
        this.clientFacade = clientFacade;
    }

    @Override
    public SendMessage handle(Message message) throws IllegalArgumentException {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();

        String clientPhoneNumber = PhoneFormatterUtils.normalizeRuPhone(message.getContact().getPhoneNumber());

        Client client = clientFacade.findByPhoneNumber(clientPhoneNumber);

        if (Objects.isNull(client)) {
            return createNewClient(clientPhoneNumber, tgUserId, chatId);
        }

        botStateService.findByTgUserIdAndSaveState(tgUserId, BotState.CARD_FOUND);
        return messageService.getReplyMessageFromSource(chatId, "reply.clientAlreadyCreated");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ENTER_PHONE_NUMBER;
    }

    private SendMessage createNewClient(String clientPhoneNumber, Long tgUserId, Long chatId) {
        clientFacade.createTemplate(clientPhoneNumber, tgUserId);

        botStateService.findByTgUserIdAndSaveState(tgUserId, BotState.ASK_SURNAME);

        return messageService.getReplyMessageFromSource(chatId, "replay.form.surname");
    }
}
