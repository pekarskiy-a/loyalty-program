package ru.sevbereg.loyaltyprogra.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.controller.api.TransactionCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Employee;
import ru.sevbereg.loyaltyprogra.domain.Transaction;
import ru.sevbereg.loyaltyprogra.facade.CardFacade;
import ru.sevbereg.loyaltyprogra.facade.TransactionFacade;
import ru.sevbereg.loyaltyprogra.mapper.TransactionMapper;
import ru.sevbereg.loyaltyprogra.service.EmployeeService;
import ru.sevbereg.loyaltyprogra.service.TransactionService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {

    private final TransactionService transactionService;

    private final TransactionMapper transactionMapper;

    private final EmployeeService employeeService;

    private final CardFacade cardFacade;

    @Override
    @Transactional
    public Transaction create(TransactionCreateRq request) {
        var transaction = transactionMapper.mapToTransaction(request);

        Transaction transactionFromDb = transactionService.findByIdempotencyKey(transaction.getIdempotencyKey());

        if (Objects.nonNull(transactionFromDb)) {
            return transactionFromDb;
        }

        Card updatedCard = cardFacade.updateTierAndBalance(request.getCardId(), request.getBonusEarned(), request.getBonusSpent());
        Employee employee = employeeService.findById(request.getEmployeeId());

        transaction.setCard(updatedCard);
        transaction.setEmployee(employee);

        return transactionService.create(transaction);
    }

}
