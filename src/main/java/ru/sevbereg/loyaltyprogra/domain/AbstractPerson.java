package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.sevbereg.loyaltyprogra.domain.tgbot.UserBotState;

import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AbstractPerson extends AbstractMutableEntity {

    @Column(name = "c_phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Email
    @Column(name = "c_email", unique = true)
    private String email;

    @Column(name = "c_sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "c_surname")
    private String surname;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_patronymic")
    private String patronymic;

    @Column(name = "c_birthdate")
    private LocalDate birthdate;

    @OneToOne
    @JoinColumn(name = "c_tg_user_id", referencedColumnName = "c_tg_user_id")
    private UserBotState botState;

}
