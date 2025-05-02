package ru.sevbereg.loyaltyprogra.tgbotapi.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;

public interface InputCallbackQueryHandler {

    /**
     * Метод обработки кнопки
     *
     * @param buttonQuery
     * @return
     */
    BotApiMethod<?> handle(CallbackQuery buttonQuery);

    BotState getHandlerName();

}
