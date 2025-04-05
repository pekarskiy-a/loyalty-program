package ru.sevbereg.loyaltyprogra.service;

import org.springframework.stereotype.Service;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;

@Service
public interface LoyaltyTierService {

    LoyaltyTier save(LoyaltyTier request);

    LoyaltyTier findById(Long id);

    void delete(LoyaltyTier request);
}
