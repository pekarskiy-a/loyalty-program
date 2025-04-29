package ru.sevbereg.loyaltyprogra.tgbotapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.ClientTgBotHandler;
import ru.sevbereg.loyaltyprogra.util.JsonUtils;

@Slf4j
@Component
public class LoyaltyProgramClientBot extends TelegramWebhookBot {

    @Value("telegram.bot.path")
    private String botPath;

    @Value("telegram.bot.username")
    private String botUsername;

    private ClientTgBotHandler clientFacade;

    public LoyaltyProgramClientBot(@Value("telegram.bot.token") String botToken, ClientTgBotHandler clientFacade) {
        super(botToken);
        this.clientFacade = clientFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        update.hasCallbackQuery(); //используется если в боте есть кнопки
        update.hasMessage(); //используется если отправляется текстовое сообщение. Надо реализовать оба.
        System.out.println(JsonUtils.toJson(update)); //todo посмотреть что можно вытащить
        return clientFacade.handleUpdate(update);
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
