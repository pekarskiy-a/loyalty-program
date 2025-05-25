package ru.sevbereg.loyaltyprogra.service;

import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;

import java.util.List;

public interface LoyaltyProgramService {

    List<LoyaltyProgram> findAll();

    List<LoyaltyProgram> findAllActive();

    LoyaltyProgram findById(String id);

    LoyaltyProgram findByLpName(String lpName);

    LoyaltyProgram save(LoyaltyProgram entity);

    LoyaltyProgram update(LoyaltyProgram entity);

    LoyaltyProgram softDelete(LoyaltyProgram entity);

    void delete(LoyaltyProgram entity);

    void deleteById(Long id);

}
