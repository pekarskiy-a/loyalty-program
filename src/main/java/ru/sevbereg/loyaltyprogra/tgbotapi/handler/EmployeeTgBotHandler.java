package ru.sevbereg.loyaltyprogra.tgbotapi.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.domain.tgbot.Role;
import ru.sevbereg.loyaltyprogra.domain.tgbot.UserBotState;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.BotStateContext;

import java.util.Optional;

import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.ASK_CLIENT_CARD;
import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.SHOW_MAIN_MENU;

@Slf4j
@Service
public class EmployeeTgBotHandler extends AbstractTgBotHandler {

    public EmployeeTgBotHandler(BotStateContext botStateContext,
                                UserBotStateService botStateService,
                                ReplyMessageService replayMessageService) {
        super(botStateContext, botStateService, replayMessageService, log);
    }

    @Override
    protected SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        try {
            BotState botState = switch (inputMessage) {
                case "/start" ->
                        botStateService.createIfNoExist(userId, chatId, SHOW_MAIN_MENU, Role.EMPLOYEE).getBotState();
                case "Информация о клиенте",
                     "Начислить/списать баллы",
                     "Отметить не заезд" -> ASK_CLIENT_CARD;
                default -> Optional.ofNullable(botStateService.getUserBotStateByTgId(userId))
                        .map(UserBotState::getBotState)
                        .orElse(SHOW_MAIN_MENU);
            };
            return botStateContext.processInputMessage(botState, message);
        } catch (Exception ex) {
            log.error("Произошла ошибка.", ex);
            return replayMessageService.getReplyMessageFromSource(chatId, "error.unknown");
        }
    }
}
