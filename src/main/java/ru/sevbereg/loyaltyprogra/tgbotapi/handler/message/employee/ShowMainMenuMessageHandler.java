package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.service.tgbot.MainMenuService;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputMessageHandler;

@Component
@RequiredArgsConstructor
public class ShowMainMenuMessageHandler implements InputMessageHandler {

    private final MainMenuService mainMenuService;

    @Override
    public SendMessage handle(Message message) {
        return mainMenuService.getEmployeeMenuMessage(message.getChatId(), "replay.employee.menu");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}
