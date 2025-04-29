package ru.sevbereg.loyaltyprogra.tgbotapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputMessageHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {

    private final Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMesssageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMesssageHandler(BotState currentState) {
        if (isFillingProcessState(currentState)) {
            return messageHandlers.get(BotState.FILLING_FORM);
        }
        return messageHandlers.get(currentState);
    }

    private boolean isFillingProcessState(BotState currentState) {
        return switch (currentState) {
            case ASK_SURNAME, ASK_NAME, ASK_PATRONYMIC, ASK_BIRTHDATE, ASK_SEX, ASK_EMAIL, FILLING_FORM -> true;
            default -> false;
        };
    }

}
