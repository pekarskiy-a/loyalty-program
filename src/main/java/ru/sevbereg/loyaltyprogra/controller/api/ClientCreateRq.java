package ru.sevbereg.loyaltyprogra.controller.api;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class ClientCreateRq {

    private Set<CardCreateRq> cards;

    private String phoneNumber;

    private String telegram;

    private String email;

    private String sex;

    private String surname;

    private String name;

    private String patronymic;

    private LocalDate birthdate;

}
