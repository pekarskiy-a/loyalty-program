package ru.sevbereg.loyaltyprogra.controller.api;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class LoyaltyProgramCreateRq {

    private String lpName;

    private String description;

    private Set<LoyaltyTierDto> tiers;

    private LocalDate startDate;

    private LocalDate endDate;

}
