package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.facade.tgbot.ClientTgBotFacade;
import ru.sevbereg.loyaltyprogra.service.tgbot.MainMenuService;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.util.PhoneFormatterUtils;

import java.util.Objects;

@Slf4j
@Component
public class EnterPhoneNumberMessageHandler extends AbstractInputMessageHandler {

    private final ClientTgBotFacade clientFacade;
    private final MainMenuService mainMenuService;

    public EnterPhoneNumberMessageHandler(UserBotStateService botStateService,
                                          ReplyMessageService messageService,
                                          ClientTgBotFacade clientFacade, MainMenuService mainMenuService) {
        super(botStateService, messageService);
        this.clientFacade = clientFacade;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) throws IllegalArgumentException {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();

        log.trace("CLIENT. Обработка номера клиента");
        String clientPhoneNumber = PhoneFormatterUtils.normalizeRuPhone(message.getContact().getPhoneNumber());
        Client client = clientFacade.findByPhoneNumber(clientPhoneNumber);

        if (Objects.isNull(client)) {
            log.trace("CLIENT. Клиент с номером: {} уже существует", clientPhoneNumber);
            return createNewClient(clientPhoneNumber, tgUserId, chatId);
        }

        botStateService.updateClientState(tgUserId, BotState.CARD_FOUND);
        return mainMenuService.getMainMenuMessage(chatId, "reply.clientAlreadyCreated");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ENTER_PHONE_NUMBER;
    }

    private SendMessage createNewClient(String clientPhoneNumber, Long tgUserId, Long chatId) {
        clientFacade.createTemplate(clientPhoneNumber, tgUserId);

        botStateService.updateClientState(tgUserId, BotState.ASK_SURNAME);

        ReplyKeyboardRemove removeKeyboard = ReplyKeyboardRemove.builder()
                .removeKeyboard(true)
                .selective(true)
                .build();

        SendMessage replyMessage = messageService.getReplyMessageFromSource(chatId, "replay.form.surname");
        replyMessage.setReplyMarkup(removeKeyboard);
        return replyMessage;
    }
}
