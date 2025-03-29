package ru.sevbereg.loyaltyprogra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AbstractPerson extends AbstractMutableEntity {

    @Column(name = "c_phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "c_telegram", unique = true)
    private String telegram;

    @Email
    @Column(name = "c_email", unique = true)
    private String email;

    @Column(name = "c_sex")
    private String sex;

    @Column(name = "c_surname")
    private String surname;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_patronymic")
    private String patronymic;

    @Column(name = "c_birthdate")
    private Instant birthdate;

}
