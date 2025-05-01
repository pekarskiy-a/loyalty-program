package ru.sevbereg.loyaltyprogra.enricher;

import org.springframework.stereotype.Component;
import ru.sevbereg.loyaltyprogra.domain.Client;
import ru.sevbereg.loyaltyprogra.domain.Sex;
import ru.sevbereg.loyaltyprogra.tgbotapi.api.UpdateClientTemplate;

import java.util.Optional;
import java.util.function.Consumer;

@Component
public class ClientTemplateEnricher {

    public Client enrich(Client source, UpdateClientTemplate target) {
        setIfPresent(target.getPhoneNumber(), source::setPhoneNumber);
        setIfPresent(target.getTelegram(), source::setTelegram);
        setIfPresent(target.getEmail(), source::setEmail);
        Sex sex = Optional.ofNullable(target.getSex())
                .map(Sex::valueOf)
                .orElse(null);
        setIfPresent(sex, source::setSex);
        setIfPresent(target.getSurname(), source::setSurname);
        setIfPresent(target.getName(), source::setSurname);
        setIfPresent(target.getName(), source::setName);
        setIfPresent(target.getPatronymic(), source::setPatronymic);
        setIfPresent(target.getBirthdate(), source::setBirthdate);
        return source;
    }

    public <T> void setIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
