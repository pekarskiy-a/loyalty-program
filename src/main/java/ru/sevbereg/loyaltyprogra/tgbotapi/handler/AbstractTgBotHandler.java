package ru.sevbereg.loyaltyprogra.tgbotapi.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractTgBotHandler {

    protected final BotStateContext botStateContext;
    protected final UserBotStateService botStateService;
    protected final ReplyMessageService replayMessageService;

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("Новый callbackQuery от пользователя userID:{}, с данными: {}",
                    callbackQuery.getFrom().getId(), callbackQuery.getData());
            return processInputCallbackQuery(callbackQuery);
        }

        final Message message = update.getMessage();
        if (Objects.isNull(message)) {
            return null;
        }

        if (!message.hasText() && !message.hasContact()) {
            log.info("Отсутствует сообщение от userID:{}, chatId: {}, с текстом: {}", message.getFrom().getUserName(), message.getChatId(), message.getText());
            return replayMessageService.getReplyMessageFromSource(message.getChatId(), "error.empty.message");
        }

        log.info("Новое сообщение от User:{}, chatId: {}, с текстом: {}", message.getFrom().getId(), message.getChatId(), message.getText());

        return handleInputMessage(message);
    }

    /**
     * Метод обработки кнопок
     *
     * @param callbackQuery
     * @return сообщение или кнопки
     */
    private BotApiMethod<?> processInputCallbackQuery(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();

        try {
            BotState botState = botStateService.getUserBotStateByTgId(userId).getBotState();
            return botStateContext.processInputCallbackQuery(botState, callbackQuery);
        } catch (Exception ex) {
            return replayMessageService.getReplyMessageFromSource(chatId, "error.unknown");
        }
    }

    /**
     * Метод обработки сообщений
     *
     * @param message
     * @return
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
