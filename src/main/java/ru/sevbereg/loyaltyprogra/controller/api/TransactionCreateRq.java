package ru.sevbereg.loyaltyprogra.controller.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionCreateRq {

    private BigDecimal bonusEarned;

    private BigDecimal bonusSpent;

    /**
     * Клиент потратил денег
     */
    private BigDecimal amountSpent;

    private String description;

    private Long cardId;

    private Long employeeId;

}
