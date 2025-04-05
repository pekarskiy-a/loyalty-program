package ru.sevbereg.loyaltyprogra.controller.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardCreateRq {

    private Long loyaltyTierId;

    private Long cardNumber;

    private BigDecimal bonusBalance;

}
