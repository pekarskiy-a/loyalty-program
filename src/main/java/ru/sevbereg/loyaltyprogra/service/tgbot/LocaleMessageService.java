package ru.sevbereg.loyaltyprogra.service.tgbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleMessageService {

    private final Locale localeTag;

    private final MessageSource messageSource;

    public LocaleMessageService(@Value("${locale.tag}") String localeTag, MessageSource messageSource) {
        this.localeTag = Locale.forLanguageTag(localeTag);
        this.messageSource = messageSource;
    }

    public String getMessage(String message) {
        return messageSource.getMessage(message, null, localeTag);
    }

    public String getMessage(String message, Object... args) {
        return messageSource.getMessage(message, args, localeTag);
    }
}
