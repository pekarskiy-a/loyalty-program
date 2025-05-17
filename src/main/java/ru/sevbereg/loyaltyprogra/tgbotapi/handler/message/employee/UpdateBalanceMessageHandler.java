package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.controller.api.TransactionCreateRq;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.facade.TransactionFacade;
import ru.sevbereg.loyaltyprogra.facade.tgbot.EmployeeTgBotFacade;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputMessageHandler;

import java.math.BigDecimal;

@RequiredArgsConstructor
public abstract class UpdateBalanceMessageHandler implements InputMessageHandler {

    private final UserBotStateService botStateService;
    private final EmployeeTgBotFacade employeeFacade;
    private final TransactionFacade transactionFacade;
    private final ReplyMessageService replyMessageService;

    @Override
    @Transactional
    public SendMessage handle(Message message) {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();

        Long updateCardId = botStateService.getUserBotStateByTgId(tgUserId).getUpdateCardId();
        Long employeeId = employeeFacade.findByTgUserId(tgUserId).getId();
        BigDecimal bonus = null;
        try {
            bonus = new BigDecimal(message.getText());
        } catch (NumberFormatException ex) {
            replyMessageService.getReplyMessageFromSource(chatId, "replay.employee.error.bigdecimal");
        }

        var transaction = buildTransactionRq(bonus, employeeId, updateCardId);
        TransactionCreateRq.builder()
                .bonusEarned(bonus)
                .employeeId(employeeId)
                .cardId(updateCardId)
                .build();

        transactionFacade.createAndSendTgMessage(transaction);
        botStateService.updateEmployeeState(tgUserId, BotState.SHOW_MAIN_MENU, null);
        return replyMessageService.getReplyMessageFromSource(chatId, "replay.employee.balance.update.success");
    }

    protected abstract TransactionCreateRq buildTransactionRq(BigDecimal bonusChange, Long employeeId, Long cardId);
}
