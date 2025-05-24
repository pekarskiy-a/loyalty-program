package ru.sevbereg.loyaltyprogra.tgbotapi.handler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.BotStateContext;

import java.util.Objects;

@RequiredArgsConstructor
public abstract class AbstractTgBotHandler {

    protected final BotStateContext botStateContext;
    protected final UserBotStateService botStateService;
    protected final ReplyMessageService replayMessageService;
    protected final Logger log;

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.trace("Начало обработки callbackQuery с данными: {}", callbackQuery.getData());
            return processInputCallbackQuery(callbackQuery);
        }

        final Message message = update.getMessage();
        if (Objects.isNull(message)) {
            log.warn("Отправлено пустое сообщение");
            return null;
        }

        if (!message.hasText() && !message.hasContact()) {
            log.trace("Пользователь поделился контактами");
            return replayMessageService.getReplyMessageFromSource(message.getChatId(), "error.empty.message");
        }

        log.trace("Начало обработки message с текстом: {}", message.getText());
        return handleInputMessage(message);
    }

    /**
     * Метод обработки кнопок
     *
     * @param callbackQuery контейнер с идентификаторами запроса
     * @return сообщение или кнопки
     */
    private BotApiMethod<?> processInputCallbackQuery(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();

        try {
            BotState botState = botStateService.getUserBotStateByTgId(userId).getBotState();
            return botStateContext.processInputCallbackQuery(botState, callbackQuery);
        } catch (Exception ex) {
            log.error("Произошла ошибка.", ex);
            return replayMessageService.getReplyMessageFromSource(chatId, "error.unknown");
        }
    }

    /**
     * Метод обработки сообщений
     *
     * @param message контейнер с идентификаторами запроса
     * @return сообщение или кнопки
     */
    protected abstract SendMessage handleInputMessage(Message message);

    /**
     * Метод создания всплывающего уведомления (выводится вместо сообщения тк является наследником BotApiMethod)
     *
     * @param text
     * @param alert
     * @param callbackQuery
     * @return
     */
    private AnswerCallbackQuery buildAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery) {
        return AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .showAlert(alert)
                .text(text)
                .build();
    }

}
