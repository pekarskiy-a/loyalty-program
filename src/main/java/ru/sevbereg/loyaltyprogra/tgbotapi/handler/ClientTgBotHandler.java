package ru.sevbereg.loyaltyprogra.tgbotapi.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sevbereg.loyaltyprogra.domain.Sex;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.domain.tgbot.ClientBotState;
import ru.sevbereg.loyaltyprogra.facade.tgbot.ClientTgBotFacade;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.BotStateContext;
import ru.sevbereg.loyaltyprogra.tgbotapi.api.UpdateClientTemplate;

import java.util.Objects;
import java.util.Optional;

import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.ASK_PHONE_NUMBER;
import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.SHOW_HELP_MENU;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientTgBotHandler {

    private final BotStateContext botStateContext;

    private final UserBotStateService botStateService;

    private final ReplyMessageService messageService;

    private final ClientTgBotFacade clientFacade;

    public BotApiMethod<?> handleUpdate(Update request) {
        if (request.hasCallbackQuery()) {
            final CallbackQuery callbackQuery = request.getCallbackQuery();
            log.info("Новый callbackQuery от пользователя userID:{}, с данными: {}",
                    request.getCallbackQuery().getFrom().getId(), request.getCallbackQuery().getData());
            return processCallBackQuery(callbackQuery);
        }


        Message message = request.getMessage();
        if (Objects.isNull(message)) {
            return null;
        }
        if (!message.hasText()) {
            log.info("Отсутствует сообщение от userID:{}, chatId: {}, с текстом: {}", message.getFrom().getUserName(), message.getChatId(), message.getText());
            return messageService.getReplyMessage(message.getChatId(), "Вы отправили пустое сообщение");
        }

        log.info("Новое сообщение от User:{}, chatId: {}, с текстом: {}", message.getFrom().getId(), message.getChatId(), message.getText());

        return handleInputMessage(message);
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        Long userId = message.getFrom().getId();

        BotState botState = switch (inputMessage) {
            case "/start" -> ASK_PHONE_NUMBER;
//            case "Зарегистрироваться в программе лояльности" -> FILLING_FORM;
            case "Помощь" -> SHOW_HELP_MENU;
            default -> Optional.ofNullable(botStateService.getUserBotStateByTgId(userId))
                    .map(ClientBotState::getBotState)
                    .orElse(ASK_PHONE_NUMBER);
        };

        botStateService.findByTgUserIdAndSaveState(userId, botState);

        return botStateContext.processInputMessage(botState, message);
    }

    /**
     * Метод обработки кнопок. В текущей реализации существует только обработка кнопок пола, поэтому доп условий нет
     *
     * @param buttonQuery
     * @return
     */
    private BotApiMethod<?> processCallBackQuery(CallbackQuery buttonQuery) {
        return processSexBottom(buttonQuery);
    }

    /**
     * Метод обработки кнопки пола
     *
     * @param buttonQuery
     * @return
     */
    private BotApiMethod<?> processSexBottom(CallbackQuery buttonQuery) {
        final Long chatId = buttonQuery.getMessage().getChatId();
        final Long userId = buttonQuery.getFrom().getId();

        try {
            clientFacade.updateClientTemplate(UpdateClientTemplate.builder().tgUserId(userId).sex(Sex.valueOf(buttonQuery.getData())).build());
            botStateService.findByTgUserIdAndSaveState(userId, BotState.ASK_EMAIL);

            return messageService.getReplyMessageFromSource(chatId, "replay.form.email");
        } catch (Exception ex) {
            return messageService.getReplyMessageFromSource(chatId, "replay.form.sex.error");
        }
    }

    /**
     * Метод создания всплывающего уведомления
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
