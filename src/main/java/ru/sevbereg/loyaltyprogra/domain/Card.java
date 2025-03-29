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
@Table(name = "t_card")
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
     * Кол-во отмененных или перенесенных заездов
     */
    @Column(name = "c_sum_cancelled_check_in")
    private Integer sumCancelledCheckIn;

    @Column(name = "c_bonus_balance")
    private BigDecimal bonusBalance;

}
