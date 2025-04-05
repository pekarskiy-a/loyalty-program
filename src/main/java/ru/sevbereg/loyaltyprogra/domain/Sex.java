package ru.sevbereg.loyaltyprogra.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Sex {
    M("М"),
    W("Ж");

    private String description;
}
