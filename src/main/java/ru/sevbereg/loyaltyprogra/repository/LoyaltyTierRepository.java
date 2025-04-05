package ru.sevbereg.loyaltyprogra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;

public interface LoyaltyTierRepository extends JpaRepository<LoyaltyTier, Long> {

}
