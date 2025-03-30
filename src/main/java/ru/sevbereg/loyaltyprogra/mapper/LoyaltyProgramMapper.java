package ru.sevbereg.loyaltyprogra.mapper;

import org.mapstruct.Mapper;
import ru.sevbereg.loyaltyprogra.controller.api.LoyaltyProgramCreateRq;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface LoyaltyProgramMapper {

    LoyaltyProgram mapToLoyaltyProgram(LoyaltyProgramCreateRq loyaltyProgramCreateRq);

//    LoyaltyTier mapToLoyaltyTier(LoyaltyTierDto loyaltyTierDto);

}
