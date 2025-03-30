package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "t_card")
@EqualsAndHashCode(callSuper = true)
public class Card extends AbstractMutableEntity {

    @ManyToOne
    @JoinColumn(name = "c_tier_id")
    private LoyaltyTier loyaltyTier;

    @ManyToOne
    @JoinColumn(name = "c_client_id")
    private Client client;

    @Column(name = "c_card_number")
    private Long cardNumber;

    /**
     * Доступность бронирования без предоплаты
     */
    @Column(name = "c_is_available_booking")
    private boolean isAvailableBooking;

    /**
     * Кол-во отмененных или перенесенных заездов
     */
    @Column(name = "c_sum_cancelled_check_in")
    private int sumCancelledCheckIn;

    @Column(name = "c_bonus_balance")
    private BigDecimal bonusBalance;

}
