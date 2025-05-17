package ru.sevbereg.loyaltyprogra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;
import ru.sevbereg.loyaltyprogra.repository.LoyaltyTierRepository;
import ru.sevbereg.loyaltyprogra.service.LoyaltyTierService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoyaltyTierServiceImpl implements LoyaltyTierService {

    private final LoyaltyTierRepository repository;

    @Override
    public LoyaltyTier save(LoyaltyTier request) {
        return repository.save(request);
    }

    @Override
    public LoyaltyTier findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<LoyaltyTier> findAllActiveOrderByNextValueAsc() {
        return repository.findAllByActiveTrueOrderByNextLevelValueAsc();
    }

    @Override
    public void delete(LoyaltyTier request) {
        repository.delete(request);
    }

}
