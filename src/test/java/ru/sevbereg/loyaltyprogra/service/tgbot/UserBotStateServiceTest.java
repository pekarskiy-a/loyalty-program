package ru.sevbereg.loyaltyprogra.service.tgbot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.domain.tgbot.Role;
import ru.sevbereg.loyaltyprogra.domain.tgbot.UserBotState;
import ru.sevbereg.loyaltyprogra.repository.tgbot.UserBotStateRepository;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserBotStateServiceTest {

    @Mock
    private UserBotStateRepository repository;

    @InjectMocks
    private UserBotStateService service;

    private UserBotState userBotState;

    @BeforeEach
    void setUp() {
        userBotState = new UserBotState(1L, 1L, BotState.FORM_FILLED, Role.CLIENT);
    }

    @Test
    void updateClientState() {
        when(repository.findByTgUserId(anyLong())).thenReturn(userBotState);
        when(repository.save(any(UserBotState.class))).thenReturn(userBotState);

        UserBotState updatedState = service.updateClientState(1L, BotState.SHOW_MAIN_MENU);

        assertEquals(BotState.SHOW_MAIN_MENU, updatedState.getBotState());
        verify(repository, times(1)).findByTgUserId(1L);
        verify(repository, times(1)).save(userBotState);
    }

    @Test
    void updateEmployeeState() {
        userBotState.setUpdateCardId(1L);
        when(repository.findByTgUserId(anyLong())).thenReturn(userBotState);
        when(repository.save(any(UserBotState.class))).thenReturn(userBotState);

        UserBotState updatedState = service.updateEmployeeState(1L, BotState.SHOW_MAIN_MENU, 1L);

        assertEquals(BotState.SHOW_MAIN_MENU, updatedState.getBotState());
        assertEquals(1L, updatedState.getUpdateCardId());
        verify(repository, times(1)).findByTgUserId(1L);
        verify(repository, times(1)).save(userBotState);
    }

    @Test
    void createIfNoExist() {
        when(repository.findByTgUserId(anyLong())).thenReturn(null);
        when(repository.save(any(UserBotState.class))).thenReturn(userBotState);

        UserBotState createdState = service.createIfNoExist(1L, 1L, BotState.FORM_FILLED, Role.CLIENT);

        assertEquals(BotState.FORM_FILLED, createdState.getBotState());
        assertEquals(Role.CLIENT, createdState.getRole());
        verify(repository, times(1)).findByTgUserId(1L);
        verify(repository, times(1)).save(userBotState);
    }

    @Test
    void saveOrUpdate() {
        when(repository.save(any(UserBotState.class))).thenReturn(userBotState);

        UserBotState savedState = service.saveOrUpdate(userBotState);

        assertEquals(userBotState, savedState);
        verify(repository, times(1)).save(userBotState);
    }

    @Test
    void getUserBotStateByTgId() {
        when(repository.findByTgUserId(anyLong())).thenReturn(userBotState);

        UserBotState foundState = service.getUserBotStateByTgId(1L);

        assertEquals(userBotState, foundState);
        verify(repository, times(1)).findByTgUserId(1L);
    }

    @Test
    void setIfPresent() {
        Consumer<Long> setter = mock(Consumer.class);
        service.setIfPresent(1L, setter);
        verify(setter, times(1)).accept(1L);
    }
}