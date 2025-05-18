package ru.sevbereg.loyaltyprogra.tgbotapi.handler.query.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.facade.CardFacade;
import ru.sevbereg.loyaltyprogra.service.tgbot.LocaleMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputCallbackQueryHandler;

@Service
@RequiredArgsConstructor
public class ClientInfoButtonsQueryHandler implements InputCallbackQueryHandler {

    private final UserBotStateService botStateService;
    private final ReplyMessageService replyMessageService;
    private final LocaleMessageService localeMessageService;
    private final CardFacade cardFacade;

    @Override
    public BotApiMethod<?> handle(CallbackQuery buttonQuery) {
        final Long chatId = buttonQuery.getMessage().getChatId();
        final Long userId = buttonQuery.getFrom().getId();

        String addBalanceButtonName = localeMessageService.getMessage("button.employee.client.balance.add");
        String writeOffBalanceButtonName = localeMessageService.getMessage("button.employee.client.balance.writeOff");
        String addCancelledCheckIn = localeMessageService.getMessage("button.employee.menu.client.addCancelledCheckIn");

        String updateBalance = buttonQuery.getData();

        if (addBalanceButtonName.equalsIgnoreCase(updateBalance)) {
            botStateService.updateEmployeeState(userId, BotState.ADD_BALANCE, null);
        } else if (writeOffBalanceButtonName.equalsIgnoreCase(updateBalance)) {
            botStateService.updateEmployeeState(userId, BotState.WRITE_OFF_BALANCE, null);
        } else if (addCancelledCheckIn.equalsIgnoreCase(updateBalance)) {
            return this.handleAddCancelledCheckIn(userId, chatId);
        }

        return replyMessageService.getReplyMessageFromSource(chatId, "replay.employee.balance.update");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CLIENT_INFO_BUTTONS;
    }

    private SendMessage handleAddCancelledCheckIn(Long userId, Long chatId) {
        Long updateCardId = botStateService.getUserBotStateByTgId(userId).getUpdateCardId();

        cardFacade.addCancelledCheckIn(updateCardId, userId);
        botStateService.updateEmployeeState(userId, BotState.SHOW_MAIN_MENU, updateCardId);
        return replyMessageService.getReplyMessageFromSource(chatId, "replay.employee.cancelledCheckIn.add.success");
    }
}
