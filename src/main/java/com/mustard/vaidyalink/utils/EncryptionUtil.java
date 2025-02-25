package com.mustard.vaidyalink.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String encryptDecryptString(String mode, String data, String key) {
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            keyBytes = Arrays.copyOf(keyBytes, 16);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            if (mode.equals("encrypt")) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(encryptedBytes);
            } else if (mode.equals("decrypt")) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
                return new String(decryptedBytes, StandardCharsets.UTF_8);
            } else {
                throw new RuntimeException("Invalid mode. Please use 'encrypt' or 'decrypt'");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error in Encryption/Decryption Process", e);
        }
    }

    private static int getTextNumericValue(String text) {
        int sum = 0;
        for (char c : text.toCharArray()) {
            sum += c;
        }
        return sum;
    }

    public static LocalDate encryptDecryptDate(String mode, LocalDate date, String key) {
        try {
            int keyValue = getTextNumericValue(key) + (key.length() * 2);
            LocalDate existingDate = LocalDate.parse(date.format(DATE_FORMATTER));
            if (mode.equals("encrypt")) {
                return existingDate.plusDays(keyValue);
            } else if (mode.equals("decrypt")) {
                return existingDate.minusDays(keyValue);
            } else {
                throw new RuntimeException("Invalid mode. Please use 'encrypt' or 'decrypt'");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in Date Encryption/Decryption Process", e);
        }
    }

    public static double encryptDecryptDouble(String mode, Double value, String key) {
        try {
            int keyValue = getTextNumericValue(key) + (key.length() * 2);
            Double doubleKey = (double) keyValue;
            if (mode.equals("encrypt")) {
                return value + doubleKey;
            } else if (mode.equals("decrypt")) {
                return value - doubleKey;
            } else {
                throw new RuntimeException("Invalid mode. Please use 'encrypt' or 'decrypt'");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in Double Encryption/Decryption Process", e);
        }
    }
}
