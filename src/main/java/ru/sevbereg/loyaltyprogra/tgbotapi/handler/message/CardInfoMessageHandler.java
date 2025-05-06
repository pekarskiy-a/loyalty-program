package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.facade.tgbot.ClientTgBotFacade;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;

@Slf4j
@Component
public class CardInfoMessageHandler extends AbstractInputMessageHandler {

    private final ClientTgBotFacade clientTgBotFacade;

    public CardInfoMessageHandler(UserBotStateService botStateService, ReplyMessageService messageService, ClientTgBotFacade clientTgBotFacade) {
        super(botStateService, messageService);
        this.clientTgBotFacade = clientTgBotFacade;
    }

    @Override
    public SendMessage handle(Message message) {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();

        Client client = clientTgBotFacade.findByTgUserId(tgUserId);
        Card card = client.getCards().stream().findFirst().orElse(null);
        var stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Номер карты: %s\n", card.getCardNumber()));
        stringBuilder.append(String.format("Баланс: %s\n", card.getBonusBalance()));
        String availableBooking;
        if (card.isAvailableBooking()) {
            availableBooking = "Да";
        } else {
            availableBooking = "Нет";
        }
        stringBuilder.append(String.format("Доступность бронирования без предоплаты: %s\n", availableBooking));
        stringBuilder.append(String.format("Кол-во отмененных или перенесенных заездов подряд: %s\n", card.getSumCancelledCheckIn()));
        return messageService.getReplyMessage(chatId, stringBuilder.toString());
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_CARD_INFO;
    }
}
