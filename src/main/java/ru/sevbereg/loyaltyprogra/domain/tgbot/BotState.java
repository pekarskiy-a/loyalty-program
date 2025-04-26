package ru.sevbereg.loyaltyprogra.domain.tgbot;

/**
 * Реализовать возможность хранения состояния бота
 */
public enum BotState {

    //todo добавить состояния (для начала запросы по анкете)
    ASK_PHONE_NUMBER,
    ENTER_PHONE_NUMBER,
    FILLING_FORM,
    FORM_FILLED,
    CARD_FOUND,
    SHOW_HELP_MENU,

    //Стадии заполнения формы
    ASK_SURNAME,
    ASK_NAME,
    ASK_PATRONYMIC,
    ASK_BIRTHDATE,
    ASK_SEX,
    ASK_EMAIL
}

