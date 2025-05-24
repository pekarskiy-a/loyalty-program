package ru.sevbereg.loyaltyprogra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "t_loyalty_program")
@EqualsAndHashCode(callSuper = true)
public class LoyaltyProgram extends AbstractMutableEntity {

    @Column(name = "c_lp_name", unique = true, nullable = false)
    private String lpName;

    @Column(name = "c_description", length = 2000)
    private String description;

    @JsonIgnoreProperties("loyaltyProgram")
    @OneToMany(mappedBy = "loyaltyProgram", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<LoyaltyTier> tiers = new HashSet<>();

    @Column(name = "c_start_date")
    private LocalDate startDate;

    @Column(name = "c_end_date")
    private LocalDate endDate;

    public LoyaltyProgram addTiers(Set<LoyaltyTier> tiers) {
        tiers.forEach(this::addTier);
        return this;
    }

    public void addTier(LoyaltyTier tier) {
        tiers.add(tier);
        tier.setLoyaltyProgram(this);
    }

}
