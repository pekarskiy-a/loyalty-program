package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.service.tgbot.MainMenuService;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputMessageHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowMainMenuMessageHandler implements InputMessageHandler {

    private final MainMenuService mainMenuService;

    @Override
    public SendMessage handle(Message message) {
        log.trace("EMPLOYEE. Обработка запроса на получение главного меню");
        return mainMenuService.getEmployeeMenuMessage(message.getChatId(), "replay.employee.menu");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}
