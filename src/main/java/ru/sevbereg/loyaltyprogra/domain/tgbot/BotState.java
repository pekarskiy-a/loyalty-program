package ru.sevbereg.loyaltyprogra.domain.tgbot;

/**
 * Реализовать возможность хранения состояния бота
 */
public enum BotState {

    ASK_PHONE_NUMBER,
    ENTER_PHONE_NUMBER,
    FILLING_FORM,

    FORM_FILLED,
    CARD_FOUND,
    SHOW_MAIN_MENU, //3-е кнопки (краткая информация по карте, информация о программе лояльности)

    ASK_CARD_INFO,
    ASK_LP_INFO,
    //Стадии заполнения формы
    ASK_SURNAME,
    ASK_NAME,
    ASK_PATRONYMIC,
    ASK_BIRTHDATE,
    ASK_SEX,
    ASK_EMAIL
}

