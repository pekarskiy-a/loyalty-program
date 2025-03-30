package ru.sevbereg.loyaltyprogra.domain;

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
@Table(name = "t_transaction")
@EqualsAndHashCode(callSuper = true)
public class Transaction extends AbstractIdentifiableEntity {

    /**
     * Ключ идемпотентности
     */
    @Column(name = "c_idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(name = "c_bonus_earned")
    private BigDecimal bonusEarned;

    @Column(name = "c_bonus_spent")
    private BigDecimal bonusSpent;

    /**
     * Клиент потратил денег
     */
    @Column(name = "c_amount_spent")
    private BigDecimal amountSpent;

    @Column(name = "с_description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_employee_sbid")
    private Employee employee;

}
