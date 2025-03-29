package ru.sevbereg.loyaltyprogra.service;

import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;

import java.util.List;

public interface LoyaltyProgramService {

    List<LoyaltyProgram> findAll();

    LoyaltyProgram findById(String id);

    LoyaltyProgram save(LoyaltyProgram entity);

    LoyaltyProgram update(LoyaltyProgram entity);

    LoyaltyProgram softDelete(LoyaltyProgram entity);

    void delete(LoyaltyProgram entity);

}
