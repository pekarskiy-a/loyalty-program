package ru.sevbereg.loyaltyprogra.tgbotapi.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.sevbereg.loyaltyprogra.domain.Sex;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.facade.tgbot.ClientTgBotFacade;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.api.UpdateClientTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FillingFormHandler extends AbstractInputMessageHandler {

    //todo подумать над заполнение формы в мапе
    private final ClientTgBotFacade clientFacade;

    public FillingFormHandler(UserBotStateService botStateService,
                              ReplyMessageService messageService,
                              ClientTgBotFacade clientFacade) {
        super(botStateService, messageService);
        this.clientFacade = clientFacade;
    }

    @Override
    public SendMessage handle(Message message) {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();
        String userAnswer = message.getText();

        BotState currentClientBotState = botStateService.getUserBotStateByTgId(tgUserId).getBotState();

        return fillingForm(currentClientBotState, userAnswer, tgUserId, chatId);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_FORM;
    }

    private SendMessage fillingForm(BotState currentClientBotState, String userAnswer, Long tgUserId, Long chatId) {
        switch (currentClientBotState) {
            case ASK_SURNAME -> {
                clientFacade.updateClientTemplate(UpdateClientTemplate.builder().tgUserId(tgUserId).surname(userAnswer).build());
                botStateService.findByTgUserIdAndSaveState(tgUserId, BotState.ASK_NAME);
                return messageService.getReplyMessageFromSource(chatId, "replay.form.name");
            }
            case ASK_NAME -> {
                clientFacade.updateClientTemplate(UpdateClientTemplate.builder().tgUserId(tgUserId).name(userAnswer).build());
                botStateService.findByTgUserIdAndSaveState(tgUserId, BotState.ASK_PATRONYMIC);
                return messageService.getReplyMessageFromSource(chatId, "replay.form.patronymic");
            }
            case ASK_PATRONYMIC -> {
                clientFacade.updateClientTemplate(UpdateClientTemplate.builder().tgUserId(tgUserId).patronymic(userAnswer).build());
                botStateService.findByTgUserIdAndSaveState(tgUserId, BotState.ASK_BIRTHDATE);
                return messageService.getReplyMessageFromSource(chatId, "replay.form.birthdate");
            }
            case ASK_BIRTHDATE -> {
                try {
                    var birthdate = LocalDate.parse(userAnswer);
                    clientFacade.updateClientTemplate(UpdateClientTemplate.builder().tgUserId(tgUserId).birthdate(birthdate).build());
                    botStateService.findByTgUserIdAndSaveState(tgUserId, BotState.ASK_SEX);
                    SendMessage replyMessage = messageService.getReplyMessageFromSource(chatId, "replay.form.sex");
                    replyMessage.setReplyMarkup(getSexMessageButtons());
                    return replyMessage;
                } catch (DateTimeParseException e) {
                    return messageService.getReplyMessageFromSource(chatId, "replay.form.birthdate.error");
                }
            }
            case ASK_SEX -> {
                try {
                    clientFacade.updateClientTemplate(UpdateClientTemplate.builder().tgUserId(tgUserId).sex(Sex.valueOf(userAnswer)).build());
                    botStateService.findByTgUserIdAndSaveState(tgUserId, BotState.ASK_EMAIL);
                    return messageService.getReplyMessageFromSource(chatId, "replay.form.email");
                } catch (Exception ex) {
                    return messageService.getReplyMessageFromSource(chatId, "replay.form.sex.error");
                }
            }
            case ASK_EMAIL -> {
                clientFacade.updateClientTemplate(UpdateClientTemplate.builder().tgUserId(tgUserId).email(userAnswer).build());
                botStateService.findByTgUserIdAndSaveState(tgUserId, BotState.FORM_FILLED);
                return messageService.getReplyMessageFromSource(chatId, "reply.clientCreated");
            }
        }
        String errorMessage = String.format("Некорректный статус заполнения формы для пользователя с tgUserId [%s]!", tgUserId);
        return messageService.getReplyMessage(chatId, errorMessage);
    }

    /**
     * Метод создания кнопок пола
     *
     * @return кнопка
     */
    private InlineKeyboardMarkup getSexMessageButtons() {
        final var menButton = InlineKeyboardButton.builder()
                .text("Мужчина")
                .callbackData(Sex.M.name())
                .build();

        final var womenButton = InlineKeyboardButton.builder()
                .text("Женщина")
                .callbackData(Sex.W.name())
                .build();

        List<InlineKeyboardButton> keyboardButtonsRowSex = new ArrayList<>();
        keyboardButtonsRowSex.add(menButton);
        keyboardButtonsRowSex.add(womenButton);

        return InlineKeyboardMarkup.builder().keyboardRow(keyboardButtonsRowSex).build();
    }
}
