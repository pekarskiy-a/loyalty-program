package ru.sevbereg.loyaltyprogra.facade;

import ru.sevbereg.loyaltyprogra.domain.Card;

import java.math.BigDecimal;

public interface CardFacade {

    /**
     * Метод обновления баланса, уровня и обнуления суммы отмененных броней подряд
     *
     * @param cardId
     * @param bonusEarned
     * @param bonusSpent
     * @return
     * @throws IllegalArgumentException
     */
    Card updateTierAndBalance(Long cardId, BigDecimal bonusEarned, BigDecimal bonusSpent) throws IllegalArgumentException;

}
