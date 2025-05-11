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

import java.util.Objects;
import java.util.Optional;

import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.ASK_CARD_INFO;
import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.ASK_LP_INFO;
import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.ASK_PHONE_NUMBER;
import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.ENTER_PHONE_NUMBER;

@Slf4j
@Service
public class ClientTgBotHandler extends AbstractTgBotHandler {

    public ClientTgBotHandler(BotStateContext botStateContext,
                              UserBotStateService botStateService,
                              ReplyMessageService replayMessageService) {
        super(botStateContext, botStateService, replayMessageService);
    }

    /**
     * Метод обработки сообщений
     *
     * @param message
     * @return
     */
    @Override
    protected SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        try {
            if (Objects.isNull(inputMessage) && message.hasContact()) {
                botStateService.updateClientState(userId, ENTER_PHONE_NUMBER);
                return botStateContext.processInputMessage(ENTER_PHONE_NUMBER, message);
            }

            //todo можно попробовать добавить переключение состояний в зависимости от роли
            BotState botState = switch (inputMessage) {
                case "/start" -> botStateService.createIfNoExist(userId, chatId, ASK_PHONE_NUMBER, Role.CLIENT).getBotState();
                case "Информация о карте" -> ASK_CARD_INFO;
                case "Информация о программе лояльности" -> ASK_LP_INFO;
                default -> Optional.ofNullable(botStateService.getUserBotStateByTgId(userId)) //todo добавить кейс с информацией, что бот не умеет работать с другим текстом
                        .map(UserBotState::getBotState)
                        .orElse(ASK_PHONE_NUMBER);
            };

            return botStateContext.processInputMessage(botState, message);
        } catch (Exception ex) {
            return replayMessageService.getReplyMessageFromSource(chatId, "error.unknown");
        }
    }

}
