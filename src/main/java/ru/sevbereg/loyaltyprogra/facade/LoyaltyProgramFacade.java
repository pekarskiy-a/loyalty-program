package ru.sevbereg.loyaltyprogra.facade;

import ru.sevbereg.loyaltyprogra.controller.api.LoyaltyProgramCreateRq;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;

public interface LoyaltyProgramFacade {

    LoyaltyProgram create(LoyaltyProgramCreateRq request);

    LoyaltyProgram findById(String id);

    LoyaltyProgram update(LoyaltyProgram request);

    void deleteById(Long id);

}
