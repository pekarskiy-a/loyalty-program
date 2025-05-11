package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message.employee;

import org.springframework.stereotype.Component;
import ru.sevbereg.loyaltyprogra.controller.api.TransactionCreateRq;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.facade.TransactionFacade;
import ru.sevbereg.loyaltyprogra.facade.tgbot.EmployeeTgBotFacade;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;

import java.math.BigDecimal;

@Component
public class AddBalanceMessageHandler extends UpdateBalanceMessageHandler {

    public AddBalanceMessageHandler(UserBotStateService botStateService,
                                    EmployeeTgBotFacade employeeFacade,
                                    TransactionFacade transactionFacade,
                                    ReplyMessageService replyMessageService) {
        super(botStateService, employeeFacade, transactionFacade, replyMessageService);
    }

    @Override
    protected TransactionCreateRq buildTransactionRq(BigDecimal bonusChange, Long employeeId, Long cardId) {
        return TransactionCreateRq.builder()
                .bonusEarned(bonusChange)
                .employeeId(employeeId)
                .cardId(cardId)
                .build();
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADD_BALANCE;
    }

}
