package ru.sevbereg.loyaltyprogra.repository.tgbot;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sevbereg.loyaltyprogra.domain.tgbot.UserBotState;

public interface UserBotStateRepository extends JpaRepository<UserBotState, Long> {

    UserBotState findByTgUserId(Long userId);

}
