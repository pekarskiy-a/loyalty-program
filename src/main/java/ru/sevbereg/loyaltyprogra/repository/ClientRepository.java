package ru.sevbereg.loyaltyprogra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sevbereg.loyaltyprogra.domain.Client;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT client FROM Client client LEFT JOIN FETCH client.cards")
    List<Client> findAll();

    @Query(value = "" +
            "SELECT client FROM Client client " +
            "LEFT JOIN FETCH client.cards cards " +
            "LEFT JOIN FETCH cards.loyaltyTier tier " +
            "LEFT JOIN FETCH tier.loyaltyProgram " +
            "WHERE client.id = ?1")
    Client findById(String id);

    @Query(value = "SELECT client FROM Client client LEFT JOIN FETCH client.cards WHERE client.phoneNumber = ?1")
    Client findByPhoneNumber(String id);

    @Query("SELECT client FROM Client client LEFT JOIN FETCH client.botState state WHERE state.tgUserId = ?1")
    Client findByBotState_TgUserId(Long tgUserId);
}
