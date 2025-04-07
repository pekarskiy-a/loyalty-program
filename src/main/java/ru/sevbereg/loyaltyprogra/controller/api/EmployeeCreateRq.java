package ru.sevbereg.loyaltyprogra.controller.api;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class EmployeeCreateRq {

    private String phoneNumber;

    private String telegram;

    private String email;

    private String sex;

    private String surname;

    private String name;

    private String patronymic;

    private LocalDate birthdate;

    private Set<Long> positionIds;
}
