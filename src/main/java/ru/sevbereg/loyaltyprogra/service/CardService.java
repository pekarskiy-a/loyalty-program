package ru.sevbereg.loyaltyprogra.service;

import ru.sevbereg.loyaltyprogra.domain.Card;

public interface CardService {

    Card update(Card entity);

    Card findById(Long id);
}
