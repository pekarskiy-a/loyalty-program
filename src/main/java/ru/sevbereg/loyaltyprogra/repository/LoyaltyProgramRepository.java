package ru.sevbereg.loyaltyprogra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;

import java.util.List;

public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgram, Long> {

    @Query(value = "SELECT loyaltyProgram FROM LoyaltyProgram loyaltyProgram LEFT JOIN FETCH loyaltyProgram.tiers")
    List<LoyaltyProgram> findAll();

    @Query(value = "SELECT loyaltyProgram FROM LoyaltyProgram loyaltyProgram LEFT JOIN FETCH loyaltyProgram.tiers WHERE loyaltyProgram.isActive = true")
    List<LoyaltyProgram> findAllActive();

    @Query(value = "SELECT loyaltyProgram FROM LoyaltyProgram loyaltyProgram LEFT JOIN FETCH loyaltyProgram.tiers WHERE loyaltyProgram.id = ?1")
    LoyaltyProgram findById(String id);

    @Query(value = "SELECT loyaltyProgram FROM LoyaltyProgram  loyaltyProgram LEFT JOIN FETCH loyaltyProgram.tiers WHERE loyaltyProgram.lpName = ?1")
    LoyaltyProgram findByLpName(String lpName);

}
