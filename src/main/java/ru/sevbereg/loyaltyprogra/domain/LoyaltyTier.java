package ru.sevbereg.loyaltyprogra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_loyalty_tier")
@EqualsAndHashCode(callSuper = true, exclude = "loyaltyProgram")
public class LoyaltyTier extends AbstractMutableEntity {

    @JsonIgnoreProperties("tiers")
    @ManyToOne(targetEntity = LoyaltyProgram.class, fetch = FetchType.LAZY)
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

    @Column(name = "c_benefit", length = 2000)
    private String benefit;

    /**
     * Доступность бронирования без предоплаты
     */
    @Column(name = "c_is_available_booking")
    private boolean isAvailableBooking;

}
