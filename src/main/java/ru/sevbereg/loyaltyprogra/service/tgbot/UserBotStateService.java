package ru.sevbereg.loyaltyprogra.service.tgbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.domain.tgbot.Role;
import ru.sevbereg.loyaltyprogra.domain.tgbot.UserBotState;
import ru.sevbereg.loyaltyprogra.repository.tgbot.UserBotStateRepository;

import java.util.Objects;
import java.util.function.Consumer;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBotStateService {

    private final UserBotStateRepository repository;

    /**
     * Метод создания и обновления статуса пользователя
     *
     * @param tgUserId
     * @param botState
     * @return
     */
    public UserBotState updateClientState(Long tgUserId, BotState botState) {
        return updateUserState(tgUserId, botState, null);
    }

    public UserBotState updateEmployeeState(Long tgUserId, BotState botState, Long cardId) {
        return updateUserState(tgUserId, botState, cardId);
    }

    public UserBotState createIfNoExist(Long tgUserId, Long tgChatId, BotState botState, Role role) {
        UserBotState userBotState = repository.findByTgUserId(tgUserId);

        if (Objects.nonNull(userBotState)) {
            userBotState.setBotState(botState);
            userBotState.setTgChatId(tgChatId);
            return userBotState;
        }

        UserBotState newState = new UserBotState(tgUserId, tgChatId, botState, role);
        return repository.save(newState);
    }

    public UserBotState saveOrUpdate(UserBotState entity) {
        return repository.save(entity);
    }

    public UserBotState getUserBotStateByTgId(Long tgUserId) {
        return repository.findByTgUserId(tgUserId);
    }

    private UserBotState updateUserState(Long tgUserId, BotState botState, Long cardId) {
        UserBotState userBotState = repository.findByTgUserId(tgUserId);
        if (Objects.isNull(userBotState)) {
            throw new RuntimeException("Статус пользователя не найден");
        }

        boolean isEqualsStatuses = userBotState.getBotState().equals(botState);
        if (isEqualsStatuses) {
            return userBotState;
        }

        userBotState.setBotState(botState);
        setIfPresent(cardId, userBotState::setUpdateCardId);
        return repository.save(userBotState);
    }

    public <T> void setIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
