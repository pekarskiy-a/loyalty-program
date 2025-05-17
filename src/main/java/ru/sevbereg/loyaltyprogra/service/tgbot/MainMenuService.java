package ru.sevbereg.loyaltyprogra.service.tgbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Сервис создания клавиатуры
 */
@Service
@RequiredArgsConstructor
public class MainMenuService {

    private final LocaleMessageService localeMessageService;

    public SendMessage getMainMenuMessage(final Long chatId, final String messageSourceKey) {
        return getMenuMessage(chatId, messageSourceKey, this::getMainMenuKeyboard);
    }

    public SendMessage getEmployeeMenuMessage(final Long chatId, final String messageSourceKey) {
        return getMenuMessage(chatId, messageSourceKey, this::getEmployeeMenuKeyboard);
    }

    private SendMessage getMenuMessage(final Long chatId, final String messageSourceKey, Supplier<ReplyKeyboardMarkup> getKeyboardFunc) {
        String text = localeMessageService.getMessage(messageSourceKey);
        return createMessageWithKeyboard(chatId, text, getKeyboardFunc.get());
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardButton cardInfo = KeyboardButton.builder().text(localeMessageService.getMessage("button.client.menu.card.info")).build();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(cardInfo);

        KeyboardButton lpInfo = KeyboardButton.builder().text(localeMessageService.getMessage("button.client.menu.lp.info")).build();
        KeyboardRow row2 = new KeyboardRow();
        row2.add(lpInfo);

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .keyboard(keyboard)
                .build();
    }

    private ReplyKeyboardMarkup getEmployeeMenuKeyboard() {
        KeyboardButton cardInfo = KeyboardButton.builder().text(localeMessageService.getMessage("button.employee.menu.client.info")).build();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(cardInfo);

        KeyboardButton addBalance = KeyboardButton.builder().text(localeMessageService.getMessage("button.employee.menu.client.change.balance")).build();
        KeyboardRow row2 = new KeyboardRow();
        row2.add(addBalance);

        //todo добавить кнопку редактирования клиента

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .keyboard(keyboard)
                .build();
    }

    private SendMessage createMessageWithKeyboard(final Long chatId, String text, final ReplyKeyboardMarkup replyKeyboardMarkup) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboardMarkup)
                .build();
    }
}
