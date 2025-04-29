package ru.sevbereg.loyaltyprogra.controller.tgbot;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sevbereg.loyaltyprogra.tgbotapi.LoyaltyProgramClientBot;

@RestController
@RequiredArgsConstructor
public class WebHookClientController {

    private final LoyaltyProgramClientBot loyaltyProgramClientBot;

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return loyaltyProgramClientBot.onWebhookUpdateReceived(update);
    }

    //todo реализовать метод Get для получения инфо по карте
}
