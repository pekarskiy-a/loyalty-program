package ru.sevbereg.loyaltyprogra.tgbotapi.handler.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyProgram;
import ru.sevbereg.loyaltyprogra.domain.LoyaltyTier;
import ru.sevbereg.loyaltyprogra.domain.tgbot.BotState;
import ru.sevbereg.loyaltyprogra.service.LoyaltyProgramService;
import ru.sevbereg.loyaltyprogra.service.tgbot.ReplyMessageService;
import ru.sevbereg.loyaltyprogra.service.tgbot.UserBotStateService;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class LpInfoMessageHandler extends AbstractInputMessageHandler {

    private final LoyaltyProgramService loyaltyProgramService;

    public LpInfoMessageHandler(UserBotStateService botStateService, ReplyMessageService messageService, LoyaltyProgramService loyaltyProgramService) {
        super(botStateService, messageService);
        this.loyaltyProgramService = loyaltyProgramService;
    }

    @Override
    public SendMessage handle(Message message) {
        log.trace("CLIENT. Обработка запроса информации о программе лояльности");

        LoyaltyProgram loyaltyProgram = loyaltyProgramService.findAllActive().get(0);
        List<LoyaltyTier> tiers = loyaltyProgram.getTiers().stream()
                .sorted(Comparator.comparingLong(LoyaltyTier::getId))
                .toList();

        var stringBuilder = new StringBuilder();
        stringBuilder.append(loyaltyProgram.getLpName()).append("\n");
        stringBuilder.append(String.format("Описание: %s\n", loyaltyProgram.getLpName()));
        stringBuilder.append("Уровни:\n");
        for (LoyaltyTier tier : tiers) {
            stringBuilder.append(String.format("%s - %s \n", tier.getTierName(), tier.getBenefit()));
            stringBuilder.append(String.format("Бонусов до следующего уровня: %s \n", tier.getNextLevelValue()));
        }
        return messageService.getReplyMessage(message.getChatId(), stringBuilder.toString());
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_LP_INFO;
    }
}
