package ru.sevbereg.loyaltyprogra.service;

import ru.sevbereg.loyaltyprogra.domain.Transaction;

public interface TransactionService {

    Transaction create(Transaction entity);

    Transaction findByIdempotencyKey(String idempotencyKey);
}
