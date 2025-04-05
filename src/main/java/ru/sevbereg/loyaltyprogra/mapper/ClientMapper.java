package ru.sevbereg.loyaltyprogra.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sevbereg.loyaltyprogra.controller.api.CardCreateRq;
import ru.sevbereg.loyaltyprogra.controller.api.ClientCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client mapToClient(ClientCreateRq clientCreateRq);

    @Mapping(target = "loyaltyTier", ignore = true)
    Card mapToCard(CardCreateRq cardCreateRq);

}
