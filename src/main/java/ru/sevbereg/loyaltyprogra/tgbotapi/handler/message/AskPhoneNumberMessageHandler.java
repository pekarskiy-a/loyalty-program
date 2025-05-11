package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.service.tgbot.LocaleMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;

import java.util.List;

@Slf4j
@Component
public class AskPhoneNumberMessageHandler extends AbstractInputMessageHandler {

    private final LocaleMessageService localeMessageService;

    public AskPhoneNumberMessageHandler(UserBotStateService botStateService, ReplyMessageService messageService, LocaleMessageService localeMessageService) {
        super(botStateService, messageService);
        this.localeMessageService = localeMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        botStateService.saveOrUpdateClientState(userId, BotState.ENTER_PHONE_NUMBER);
        SendMessage replyMessage = messageService.getReplyMessageFromSource(chatId, "reply.askPhoneNumber");
        replyMessage.setReplyMarkup(buildSharePhoneMarkup());
        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_PHONE_NUMBER;
    }

    private ReplyKeyboardMarkup buildSharePhoneMarkup() {
        var requestPhoneButton = new KeyboardButton(localeMessageService.getMessage("button.phone.share"));
        requestPhoneButton.setRequestContact(true);

        KeyboardRow keyboardButtons = new KeyboardRow(List.of(requestPhoneButton));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardButtons))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .selective(true)
                .build();
    }

}
