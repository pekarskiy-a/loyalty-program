package ru.sevbereg.loyaltyprogra.service.tgbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.domain.tgbot.ClientBotState;
import ru.sevbereg.loyaltyprogra.repository.tgbot.ClientBotStateRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserBotStateService {

    private final ClientBotStateRepository repository;

    /**
     * Метод создания и обновления статуса пользователя
     *
     * @param tgUserId
     * @param botState
     * @return
     */
    @Transactional
    public ClientBotState findByTgUserIdAndSaveState(Long tgUserId, BotState botState) {
        ClientBotState clientBotState = repository.findByTgUserId(tgUserId);
        boolean isEqualsStatuses = Objects.nonNull(clientBotState) && clientBotState.getBotState().equals(botState);
        if (isEqualsStatuses) {
            return clientBotState;
        } else if (Objects.nonNull(clientBotState)) {
            clientBotState.setBotState(botState);
        } else {
            clientBotState = new ClientBotState(tgUserId, botState);
        }
        return repository.save(clientBotState);
    }

    public ClientBotState saveOrUpdate(ClientBotState entity) {
        return repository.save(entity);
    }

    public ClientBotState getUserBotStateByTgId(Long tgUserId) {
        return repository.findByTgUserId(tgUserId);
    }

}
