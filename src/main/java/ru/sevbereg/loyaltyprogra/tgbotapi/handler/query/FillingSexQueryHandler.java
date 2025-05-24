package ru.sevbereg.loyaltyprogra.tgbotapi.handler.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.facade.tgbot.ClientTgBotFacade;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;
import ru.sevbereg.loyaltyprogra.tgbotapi.api.UpdateClientTemplate;
import ru.sevbereg.loyaltyprogra.tgbotapi.handler.InputCallbackQueryHandler;

import static ru.sevbereg.loyaltyprogra.domain.tgbot.BotState.ASK_SEX;

@Slf4j
@Service
@RequiredArgsConstructor
public class FillingSexQueryHandler implements InputCallbackQueryHandler {

    private final UserBotStateService botStateService;
    private final ClientTgBotFacade clientFacade;
    private final ReplyMessageService messageService;

    @Override
    public BotApiMethod<?> handle(CallbackQuery buttonQuery) {
        final Long chatId = buttonQuery.getMessage().getChatId();
        final Long userId = buttonQuery.getFrom().getId();

        log.trace("CLIENT. Обработка кнопки пола");
        clientFacade.updateClientTemplate(UpdateClientTemplate.builder().tgUserId(userId).sex(buttonQuery.getData()).build());
        botStateService.updateClientState(userId, BotState.ASK_EMAIL);

        return messageService.getReplyMessageFromSource(chatId, "replay.form.email");
    }

    @Override
    public BotState getHandlerName() {
        return ASK_SEX;
    }
}
