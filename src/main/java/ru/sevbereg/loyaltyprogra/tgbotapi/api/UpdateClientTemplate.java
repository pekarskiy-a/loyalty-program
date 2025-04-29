package ru.sevbereg.loyaltyprogra.tgbotapi.api;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import ru.sevbereg.loyaltyprogra.domain.Sex;

import java.time.LocalDate;

/**
 * ДТО обновления клиента из шаблона
 */
@Data
@Builder
public class UpdateClientTemplate {

    private Long tgUserId;

    private String phoneNumber;

    private String telegram;

    @Email
    private String email;

    private Sex sex;

    private String surname;

    private String name;

    private String patronymic;

    private LocalDate birthdate;

}
