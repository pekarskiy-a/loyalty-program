package ru.sevbereg.loyaltyprogra.tgbotapi.handler.query.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.service.tgbot.LocaleMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputCallbackQueryHandler;

@Service
@RequiredArgsConstructor
public class UpdateBonusBalanceQueryHandler implements InputCallbackQueryHandler {

    private final UserBotStateService botStateService;
    private final ReplyMessageService replyMessageService;
    private final LocaleMessageService localeMessageService;

    @Override
    public BotApiMethod<?> handle(CallbackQuery buttonQuery) {
        final Long chatId = buttonQuery.getMessage().getChatId();
        final Long userId = buttonQuery.getFrom().getId();

        String addBalanceButtonName = localeMessageService.getMessage("button.employee.client.balance.add");
        String writeOffBalanceButtonName = localeMessageService.getMessage("button.employee.client.balance.writeOff");

        String updateBalance = buttonQuery.getData();

        if (addBalanceButtonName.equalsIgnoreCase(updateBalance)) {
            botStateService.saveOrUpdateEmployeeState(userId, BotState.ADD_BALANCE, null);
        } else if (writeOffBalanceButtonName.equalsIgnoreCase(updateBalance)) {
            botStateService.saveOrUpdateEmployeeState(userId, BotState.WRITE_OFF_BALANCE, null);
        }
        return replyMessageService.getReplyMessageFromSource(chatId, "replay.employee.balance.update");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.UPDATE_BONUS_BALANCE;
    }
}
