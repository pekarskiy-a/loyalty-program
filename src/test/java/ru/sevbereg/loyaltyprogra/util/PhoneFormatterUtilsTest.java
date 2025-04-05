package ru.sevbereg.loyaltyprogra.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static ru.sevbereg.loyaltyprogra.util.PhoneFormatterUtils.normalizeRuPhone;

class PhoneFormatterUtilsTest {

    @Test
    void normalizeRuPhoneNumberSuccess() {
        Assertions.assertEquals("79998887766", normalizeRuPhone("f8 (999) 888-77-66"));
        Assertions.assertEquals("79998887766", normalizeRuPhone("89998887766"));
        Assertions.assertEquals("79998887766", normalizeRuPhone("+7(999) 888-77-66"));
        Assertions.assertEquals("79998887766", normalizeRuPhone("79998887766"));
        Assertions.assertEquals("79998887766", normalizeRuPhone("+79998887766"));
    }

    @Test
    void normalizeRuPhoneNumberError() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> normalizeRuPhone("8 (999) 888-77-666")); //больше 11 цифр
        Assertions.assertThrows(IllegalArgumentException.class, () -> normalizeRuPhone("+375 29 123-45-67")); //регион != RU
    }

}