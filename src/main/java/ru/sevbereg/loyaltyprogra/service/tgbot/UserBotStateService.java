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
    public UserBotState saveOrUpdateClientState(Long tgUserId, BotState botState) {
        return saveOrUpdateUserState(tgUserId, botState, Role.CLIENT, null);
    }

    public UserBotState saveOrUpdateEmployeeState(Long tgUserId, BotState botState, Long cardId) {
        return saveOrUpdateUserState(tgUserId, botState, Role.EMPLOYEE, cardId);
    }

    public UserBotState saveOrUpdate(UserBotState entity) {
        return repository.save(entity);
    }

    public UserBotState getUserBotStateByTgId(Long tgUserId) {
        return repository.findByTgUserId(tgUserId);
    }

    private UserBotState saveOrUpdateUserState(Long tgUserId, BotState botState, Role role, Long cardId) {
        UserBotState userBotState = repository.findByTgUserId(tgUserId);
        boolean isEqualsStatuses = Objects.nonNull(userBotState) && userBotState.getBotState().equals(botState);
        if (isEqualsStatuses) {
            return userBotState;
        } else if (Objects.nonNull(userBotState)) {
            userBotState.setBotState(botState);
            setIfPresent(cardId, userBotState::setUpdateCardId);
        } else {
            userBotState = new UserBotState(tgUserId, botState, role);
            setIfPresent(cardId, userBotState::setUpdateCardId);
        }
        return repository.save(userBotState);
    }

    public <T> void setIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
