package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_loyalty_program")
public class LoyaltyProgram extends AbstractMutableEntity {

    /**
     * Ключ идемпотентности
     */
    @Column(name = "c_external_id", nullable = false, unique = true)
    private UUID externalId;

    @Column(name = "c_lp_name")
    private String lpName;

    @Column(name = "c_description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "lt_loyalty_program_loyalty_tier",
            joinColumns = @JoinColumn(name = "c_loyalty_program_id"),
            inverseJoinColumns = @JoinColumn(name = "c_loyalty_tier_id")
    )
    private Set<LoyaltyTier> tiers;

    @Column(name = "c_start_date")
    private LocalDate startDate;

    @Column(name = "c_end_date")
    private LocalDate endDate;

}
