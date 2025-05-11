package ru.sevbereg.loyaltyprogra.tgbotapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.sevbereg.loyaltyprogra.domain.Employee;
import ru.sevbereg.loyaltyprogra.facade.tgbot.EmployeeTgBotFacade;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.ClientTgBotHandler;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.EmployeeTgBotHandler;
import ru.sevbereg.loyaltyprogra.util.JsonUtils;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class LoyaltyProgramBot extends TelegramWebhookBot {

    @Value("telegram.bot.path")
    private String botPath;

    @Value("telegram.bot.username")
    private String botUsername;

    private final EmployeeTgBotFacade employeeFacade;
    private final ClientTgBotHandler clientHandler;
    private final EmployeeTgBotHandler employeeHandler;

    public LoyaltyProgramBot(@Value("telegram.bot.token") String botToken, EmployeeTgBotFacade employeeFacade, ClientTgBotHandler clientHandler, EmployeeTgBotHandler employeeHandler) {
        super(botToken);
        this.employeeFacade = employeeFacade;
        this.clientHandler = clientHandler;
        this.employeeHandler = employeeHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Long tgUserId = this.getTgUserId(update);

        if (Objects.nonNull(tgUserId)) {
            Employee employee = employeeFacade.findByTgUserId(tgUserId);
            if (Objects.nonNull(employee)) {
                return employeeHandler.handleUpdate(update);
            }
        }
        System.err.println(JsonUtils.toJson(update)); //todo посмотреть что можно вытащить
        return clientHandler.handleUpdate(update);
    }

    private Long getTgUserId(Update update) {
        Long tgUserId = Optional.ofNullable(update)
                .map(Update::getMessage)
                .map(Message::getFrom)
                .map(User::getId)
                .orElse(null);

        if (Objects.isNull(tgUserId)) {
            tgUserId = Optional.ofNullable(update)
                    .map(Update::getCallbackQuery)
                    .map(CallbackQuery::getFrom)
                    .map(User::getId)
                    .orElse(null);
        }
        return tgUserId;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

}
