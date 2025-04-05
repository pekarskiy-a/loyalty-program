package ru.sevbereg.loyaltyprogra.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneFormatterUtils {

    private static final String REGION_CODE_RU = "RU";

    public static String normalizeRuPhone(String rawPhone) {
        var invalidExMessage = String.format("Невалидный номер: %s. Регион должен соответствовать RU(8 или +7). Пример +7 (999) 888-77-66.", rawPhone);
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber number = util.parse(rawPhone, REGION_CODE_RU);

            if (!util.isValidNumberForRegion(number, REGION_CODE_RU)) {
                throw new IllegalArgumentException(invalidExMessage);
            }
            return util.format(number, PhoneNumberUtil.PhoneNumberFormat.E164).replace("+", "");
        } catch (NumberParseException e) {
            throw new IllegalArgumentException(invalidExMessage, e);
        }
    }

}
