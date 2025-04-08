package ru.sevbereg.loyaltyprogra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sevbereg.loyaltyprogra.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT trasnaction FROM Transaction trasnaction WHERE trasnaction.idempotencyKey = ?1")
    Transaction findByIdempotencyKey(String idempotencyKey);
}
