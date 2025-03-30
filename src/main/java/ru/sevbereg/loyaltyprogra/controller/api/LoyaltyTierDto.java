package ru.sevbereg.loyaltyprogra.controller.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoyaltyTierDto {

    private String tierName;

    private int discountPercent;

    private BigDecimal nextLevelValue;

    private String benefit;

}
