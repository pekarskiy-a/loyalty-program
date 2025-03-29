package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_loyalty_tier")
public class LoyaltyTier extends AbstractMutableEntity {

    @ManyToOne
    @JoinColumn(name = "c_loyalty_program_id")
    private LoyaltyProgram loyaltyProgram;

    /**
     * Имя уровня
     */
    @Column(name = "c_tier_name")
    private String tierName;

    /**
     * Скидка в процентах
     */
    @Column(name = "c_discount_percent")
    private int discountPercent;

    @Column(name = "c_next_level_value")
    private BigDecimal nextLevelValue;

    @Column(name = "c_benefit")
    private String benefit;
    
}
