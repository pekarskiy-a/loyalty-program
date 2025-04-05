package ru.sevbereg.loyaltyprogra.mapper;

import org.mapstruct.Mapper;
import ru.sevbereg.loyaltyprogra.controller.api.LoyaltyProgramCreateRq;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;

@Mapper(componentModel = "spring")
public interface LoyaltyProgramMapper {

    LoyaltyProgram mapToLoyaltyProgram(LoyaltyProgramCreateRq loyaltyProgramCreateRq);

}
