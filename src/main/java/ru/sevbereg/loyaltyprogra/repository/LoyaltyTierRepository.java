package ru.sevbereg.loyaltyprogra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;

import java.util.List;

public interface LoyaltyTierRepository extends JpaRepository<LoyaltyTier, Long> {

    @Query(value = "SELECT tier FROM LoyaltyTier tier LEFT JOIN FETCH tier.loyaltyProgram WHERE tier.isActive = true ORDER BY tier.nextLevelValue ASC")
    List<LoyaltyTier> findAllByActiveTrueOrderByNextLevelValueAsc();

}
