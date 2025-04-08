package ru.sevbereg.loyaltyprogra.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.controller.api.TransactionCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Employee;
import ru.sevbereg.loyaltyprogra.domain.Transaction;
import ru.sevbereg.loyaltyprogra.facade.TransactionFacade;
import ru.sevbereg.loyaltyprogra.mapper.TransactionMapper;
import ru.sevbereg.loyaltyprogra.service.CardService;
import ru.sevbereg.loyaltyprogra.service.EmployeeService;
import ru.sevbereg.loyaltyprogra.service.TransactionService;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {

    private final TransactionService transactionService;

    private final TransactionMapper transactionMapper;

    private final EmployeeService employeeService;

    private final CardService cardService;

    @Override
    @Transactional
    public Transaction create(TransactionCreateRq request) {
        var transaction = transactionMapper.mapToTransaction(request);

        Transaction transactionFromDb = transactionService.findByIdempotencyKey(transaction.getIdempotencyKey());

        if (Objects.nonNull(transactionFromDb)) {
            return transactionFromDb;
        }

        Card cardForUpdate = cardService.findById(request.getCardId());

        if (Objects.isNull(cardForUpdate)) {
            throw new IllegalArgumentException("Карта не найдена. ID: " + request.getCardId());
        }

        BigDecimal balanceChange = cardForUpdate.getBonusBalance();

        if (Objects.nonNull(request.getBonusEarned())) {
            balanceChange = balanceChange.add(request.getBonusEarned());
        }
        if (Objects.nonNull(request.getBonusSpent())) {
            balanceChange = balanceChange.subtract(request.getBonusSpent());
        }

        cardForUpdate.setBonusBalance(balanceChange);

        Employee employee = employeeService.findById(request.getEmployeeId());

        transaction.setCard(cardForUpdate);
        transaction.setEmployee(employee);

        return transactionService.create(transaction);
    }

}
