package ru.sevbereg.loyaltyprogra.repository.tgbot;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sevbereg.loyaltyprogra.domain.tgbot.ClientBotState;

public interface ClientBotStateRepository extends JpaRepository<ClientBotState, Long> {

    ClientBotState findByTgUserId(Long userId);

}
