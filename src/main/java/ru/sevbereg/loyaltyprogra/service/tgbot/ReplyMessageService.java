package ru.sevbereg.loyaltyprogra.service.tgbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class ReplyMessageService {

    private final LocaleMessageService localeMessageService;

    public SendMessage getReplyMessageFromSource(long chatId, String replyMessage) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(localeMessageService.getMessage(replyMessage))
                .build();
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(replyMessage)
                .build();
    }

    public SendMessage getReplyMessageFromSource(long chatId, String replyMessage, Object... args) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(localeMessageService.getMessage(replyMessage, args))
                .build();
    }
}
