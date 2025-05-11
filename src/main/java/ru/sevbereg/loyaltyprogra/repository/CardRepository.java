package ru.sevbereg.loyaltyprogra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sevbereg.loyaltyprogra.domain.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByCardNumber(Long cardNumber);

}
