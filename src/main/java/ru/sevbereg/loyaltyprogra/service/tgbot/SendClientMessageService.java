package ru.sevbereg.loyaltyprogra.service.tgbot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sevbereg.loyaltyprogra.tgbotapi.TelegramSender;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendClientMessageService {

    private final TelegramSender telegramSender;
    private final LocaleMessageService localeMessageService;

    public void sendBalanceUpdated(Long clientChatId, BigDecimal bonusEarned, BigDecimal bonusSpent, BigDecimal newBalance) {
        String text = null;
        if (Objects.nonNull(bonusEarned)) {
            text = localeMessageService.getMessage("replay.client.balance.add").formatted(bonusEarned, newBalance);
        } else if (Objects.nonNull(bonusSpent)) {
            text = localeMessageService.getMessage("replay.client.balance.writeOff").formatted(bonusSpent, newBalance);
        }
        if (Objects.isNull(clientChatId) || Objects.isNull(text)) {
            log.info("Сообщение не отправлено в ТГ. Отсутствует текст сообщения или chatId");
            return;
        }
        telegramSender.sendMessage(clientChatId, text);
    }
}
