package ru.sevbereg.loyaltyprogra.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.controller.api.TransactionCreateRq;
import ru.sevbereg.loyaltyprogra.domain.AbstractPerson;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Employee;
import ru.sevbereg.loyaltyprogra.domain.Transaction;
import ru.sevbereg.loyaltyprogra.domain.tgbot.UserBotState;
import ru.sevbereg.loyaltyprogra.facade.CardFacade;
import ru.sevbereg.loyaltyprogra.facade.TransactionFacade;
import ru.sevbereg.loyaltyprogra.mapper.TransactionMapper;
import ru.sevbereg.loyaltyprogra.service.EmployeeService;
import ru.sevbereg.loyaltyprogra.service.TransactionService;
import ru.sevbereg.loyaltyprogra.service.tgbot.SendClientMessageService;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final EmployeeService employeeService;
    private final CardFacade cardFacade;
    private final SendClientMessageService sendClientMessageService;

    @Override
    @Transactional
    public Transaction createAndSendTgMessage(TransactionCreateRq request) {
        log.info("Начало транзакции по cardId: {}", request.getCardId());
        var transaction = transactionMapper.mapToTransaction(request);

        Transaction transactionFromDb = transactionService.findByIdempotencyKey(transaction.getIdempotencyKey());

        if (Objects.nonNull(transactionFromDb)) {
            log.info("Транзакция найдена по ключу идемпотентности: {}", transaction.getIdempotencyKey());
            return transactionFromDb;
        }

        Card updatedCard = cardFacade.updateTierAndBalance(request.getCardId(), request.getBonusEarned(), request.getBonusSpent());
        Employee employee = employeeService.findById(request.getEmployeeId());

        transaction.setCard(updatedCard);
        transaction.setEmployee(employee);

        Optional.ofNullable(updatedCard.getClient())
                .map(AbstractPerson::getBotState)
                .map(UserBotState::getTgChatId)
                .ifPresent(chatId ->
                        sendClientMessageService.sendBalanceUpdated(chatId, request.getBonusEarned(), request.getBonusSpent(), updatedCard.getBonusBalance()));

        Transaction createdTransaction = transactionService.create(transaction);
        log.info("Транзакция по cardId: {} выполнена успешно", request.getCardId());
        return createdTransaction;
    }

}
