package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.facade.tgbot.ClientTgBotFacade;
import ru.sevbereg.loyaltyprogra.service.tgbot.LocaleMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputMessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.ENTER_PHONE_OR_CARD_ID;

@Component
@RequiredArgsConstructor
public class EnterPhoneOrCardMessageHandler implements InputMessageHandler {

    private final UserBotStateService botStateService;
    private final ReplyMessageService messageService;
    private final ClientTgBotFacade clientFacade;
    private final LocaleMessageService localeMessageService;

    @Override
    @Transactional
    public SendMessage handle(Message message) {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();
        String userAnswer = message.getText();

        Client client = clientFacade.findByPhoneOrCardNumber(userAnswer);

        if (Objects.isNull(client)) {
            String messageFromSource = localeMessageService.getMessage("replay.employee.error.noClient");
            return messageService.getReplyMessage(chatId, messageFromSource.formatted(userAnswer));
        }


        Card card = client.getCards().stream().findFirst().get();

        var stringBuilder = new StringBuilder();
        stringBuilder.append("Имя: %s\n".formatted(client.getName()));
        stringBuilder.append("Фамилия: %s\n".formatted(client.getSurname()));
        stringBuilder.append("Телефон: %s\n".formatted(client.getPhoneNumber()));
        stringBuilder.append("Номер карты: %s\n".formatted(card.getCardNumber()));
        stringBuilder.append("Баланс: %s\n".formatted(card.getBonusBalance()));
        stringBuilder.append("Без предоплаты: %s\n".formatted(card.isAvailableBooking()));
        stringBuilder.append("Кол-во незаездов: %s\n".formatted(card.getSumCancelledCheckIn()));

        botStateService.updateEmployeeState(tgUserId, BotState.UPDATE_BONUS_BALANCE, card.getId()); //todo может возникнуть уязвимость при вводе без нажатия кнопки
        SendMessage replyMessage = messageService.getReplyMessage(chatId, stringBuilder.toString());
        replyMessage.setReplyMarkup(getMessageButtons());
        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return ENTER_PHONE_OR_CARD_ID;
    }

    /**
     * Метод создания кнопок
     *
     * @return кнопки
     */
    private InlineKeyboardMarkup getMessageButtons() {
        String addBalanceButtonName = localeMessageService.getMessage("button.employee.client.balance.add");
        String writeOffBalanceButtonName = localeMessageService.getMessage("button.employee.client.balance.writeOff");

        final var addBalanceButton = InlineKeyboardButton.builder()
                .text(addBalanceButtonName)
                .callbackData(addBalanceButtonName)
                .build();

        final var writeOffBalanceButton = InlineKeyboardButton.builder()
                .text(writeOffBalanceButtonName)
                .callbackData(writeOffBalanceButtonName)
                .build();

        List<InlineKeyboardButton> keyboardButtonsRowSex = new ArrayList<>();
        keyboardButtonsRowSex.add(addBalanceButton);
        keyboardButtonsRowSex.add(writeOffBalanceButton);

        return InlineKeyboardMarkup.builder().keyboardRow(keyboardButtonsRowSex).build();
    }
}
