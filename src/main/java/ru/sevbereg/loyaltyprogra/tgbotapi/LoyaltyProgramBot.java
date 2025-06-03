package ru.sevbereg.loyaltyprogra.tgbotapi;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
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
import java.util.UUID;

@Slf4j
public class LoyaltyProgramBot extends TelegramLongPollingBot {


    private final String botUsername;
    private final EmployeeTgBotFacade employeeFacade;
    private final ClientTgBotHandler clientHandler;
    private final EmployeeTgBotHandler employeeHandler;

    public LoyaltyProgramBot(String botToken,
                             String botUsername,
                             EmployeeTgBotFacade employeeFacade,
                             ClientTgBotHandler clientHandler,
                             EmployeeTgBotHandler employeeHandler) {
        super(botToken);
        this.botUsername = botUsername;
        this.employeeFacade = employeeFacade;
        this.clientHandler = clientHandler;
        this.employeeHandler = employeeHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String rqUid = UUID.randomUUID().toString();
        Long tgUserId = this.getTgUserId(update);

        MDC.put("rqUid", rqUid);
        MDC.put("tgUserId", String.valueOf(tgUserId));

        try {
            if (Objects.nonNull(tgUserId)) {
                Employee employee = employeeFacade.findByTgUserId(tgUserId);
                if (Objects.nonNull(employee)) {
                    log.info("Получено сообщение от сотрудника");
                    BotApiMethod<?> message = employeeHandler.handleUpdate(update);
                    log.debug("Сообщение отправлено: {}{}", System.lineSeparator(), JsonUtils.toJson(message));
                    sendMessage(message);
                    return;
                }
            }

            log.info("Получено сообщение от клиента");
            log.debug("Сообщение от клиента: {}{}", System.lineSeparator(), JsonUtils.toJson(update));
            BotApiMethod<?> message = clientHandler.handleUpdate(update);
            sendMessage(message);
        } finally {
            MDC.clear();
        }
    }

    private void sendMessage(BotApiMethod<?> message) {
        try {
            log.debug("Сообщение отправлено: {}{}", System.lineSeparator(), JsonUtils.toJson(message));
            execute(message);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения", e);
        }
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

}
