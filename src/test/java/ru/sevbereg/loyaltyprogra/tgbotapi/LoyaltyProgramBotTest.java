package ru.sevbereg.loyaltyprogra.tgbotapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.sevbereg.loyaltyprogra.domain.Employee;
import ru.sevbereg.loyaltyprogra.facade.tgbot.EmployeeTgBotFacade;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.ClientTgBotHandler;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.EmployeeTgBotHandler;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoyaltyProgramBotTest {

    @Mock
    private EmployeeTgBotFacade employeeFacade;

    @Mock
    private ClientTgBotHandler clientHandler;

    @Mock
    private EmployeeTgBotHandler employeeHandler;

    @InjectMocks
    private LoyaltyProgramBot loyaltyProgramBot;

    private Update update;
    private Employee employee;

    @BeforeEach
    void setUp() {
        update = new Update();
        employee = new Employee();
    }

    @Test
    void onWebhookUpdateReceived_employee() {
        Long tgUserId = 1L;
        update.setMessage(new Message());
        update.getMessage().setFrom(new User());
        update.getMessage().getFrom().setId(tgUserId);
        employee.setId(tgUserId);

        when(employeeFacade.findByTgUserId(tgUserId)).thenReturn(employee);

        loyaltyProgramBot.onWebhookUpdateReceived(update);

        verify(employeeFacade, times(1)).findByTgUserId(tgUserId);
        verify(employeeHandler, times(1)).handleUpdate(update);
        verify(clientHandler, never()).handleUpdate(update);
    }

    @Test
    void onWebhookUpdateReceived_client() {
        Long tgUserId = 1L;
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setFrom(new User());
        update.getCallbackQuery().getFrom().setId(tgUserId);

        when(employeeFacade.findByTgUserId(tgUserId)).thenReturn(null);

        loyaltyProgramBot.onWebhookUpdateReceived(update);

        verify(employeeFacade, times(1)).findByTgUserId(tgUserId);
        verify(clientHandler, times(1)).handleUpdate(update);
        verify(employeeHandler, never()).handleUpdate(update);
    }

}