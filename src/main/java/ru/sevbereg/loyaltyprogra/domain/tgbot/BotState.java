package ru.sevbereg.loyaltyprogra.domain.tgbot;

/**
 * Реализовать возможность хранения состояния бота
 */
public enum BotState {

    //общие стадии
    SHOW_MAIN_MENU, //3-е кнопки (краткая информация по карте, информация о программе лояльности)

    //стадии клиента
    ASK_PHONE_NUMBER,
    ENTER_PHONE_NUMBER,
    FILLING_FORM,

    FORM_FILLED,
    CARD_FOUND,

    //Стадии меню
    ASK_CARD_INFO,
    ASK_LP_INFO,
    //Стадии заполнения формы
    ASK_SURNAME,
    ASK_NAME,
    ASK_PATRONYMIC,
    ASK_BIRTHDATE,
    ASK_SEX,
    ASK_EMAIL,

    //стадии администратора (чтобы реализовать доступ по ролям можно в качестве значения добавить роль)
    ASK_CLIENT_CARD,
    ENTER_PHONE_OR_CARD_ID,
    ASK_CLIENT_ID,
    ENTER_CLIENT_DATA_FOR_UPDATE,
    UPDATE_BONUS_BALANCE,
    ADD_BALANCE,
    WRITE_OFF_BALANCE

}

