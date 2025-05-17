package ru.sevbereg.loyaltyprogra.service;

import org.springframework.stereotype.Service;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;

import java.util.List;

@Service
public interface LoyaltyTierService {

    LoyaltyTier save(LoyaltyTier request);

    LoyaltyTier findById(Long id);

    List<LoyaltyTier> findAllActiveOrderByNextValueAsc();

    void delete(LoyaltyTier request);
}
