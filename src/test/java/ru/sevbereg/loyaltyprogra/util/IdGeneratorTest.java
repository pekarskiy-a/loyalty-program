package ru.sevbereg.loyaltyprogra.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class IdGeneratorTest {

    @Test
    void generateIdempotencyKey() throws NoSuchAlgorithmException {
        String rowString = "test";
        String expectedHex = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        String actualHex = IdGenerator.generateIdempotencyKey(rowString);
        assertEquals(expectedHex, actualHex);
    }

    @Test
    void generateCardNumber() {
        String phoneNumber = "1234567890";
        long expectedCardNumber = 639479525;
        long actualCardNumber = IdGenerator.generateCardNumber(phoneNumber);
        assertEquals(expectedCardNumber, actualCardNumber);
    }

}