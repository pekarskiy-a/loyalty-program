package ru.sevbereg.loyaltyprogra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.repository.CardRepository;
import ru.sevbereg.loyaltyprogra.service.CardService;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository repository;

    @Override
    public Card update(Card entity) {
        return repository.save(entity);
    }

    @Override
    public Card findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
