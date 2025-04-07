package ru.sevbereg.loyaltyprogra.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sevbereg.loyaltyprogra.controller.api.CardCreateRq;
import ru.sevbereg.loyaltyprogra.controller.api.ClientCreateRq;
import ru.sevbereg.loyaltyprogra.domain.Card;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.util.PhoneFormatterUtils;

@Mapper(componentModel = "spring", imports = PhoneFormatterUtils.class)
public interface ClientMapper {

    @Mapping(target = "phoneNumber", expression = "java(PhoneFormatterUtils.normalizeRuPhone(clientCreateRq.getPhoneNumber()))")
    Client mapToClient(ClientCreateRq clientCreateRq);

    @Mapping(target = "loyaltyTier", ignore = true)
    Card mapToCard(CardCreateRq cardCreateRq);

}
