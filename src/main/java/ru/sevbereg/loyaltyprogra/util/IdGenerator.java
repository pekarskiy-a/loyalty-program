package ru.sevbereg.loyaltyprogra.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

@UtilityClass
public class IdGenerator {

    /**
     * Генерация хэша (сохранил пока тут, чтобы не потерять)
     *
     * @param rowString
     * @return
     */
    public String generateIdempotencyKey(String rowString) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rowString.getBytes(StandardCharsets.UTF_8));
            var hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

    public long generateCardNumber(String phoneNumber) {
        CRC32 crc = new CRC32();
        crc.update(phoneNumber.getBytes());
        return crc.getValue();
    }
}
