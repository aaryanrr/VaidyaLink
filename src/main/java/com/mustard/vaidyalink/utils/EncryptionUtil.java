package com.mustard.vaidyalink.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String encryptDecryptString(String mode, String data, String key) {
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            keyBytes = Arrays.copyOf(keyBytes, 16);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

            if (mode.equals("encrypt")) {
                Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                byte[] iv = new byte[16];
                SecureRandom random = new SecureRandom();
                random.nextBytes(iv);
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
                byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
                byte[] combined = new byte[iv.length + encryptedBytes.length];
                System.arraycopy(iv, 0, combined, 0, iv.length);
                System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
                return Base64.getEncoder().encodeToString(combined);
            } else if (mode.equals("decrypt")) {
                byte[] combined = Base64.getDecoder().decode(data);
                byte[] iv = Arrays.copyOfRange(combined, 0, 16);
                byte[] encryptedBytes = Arrays.copyOfRange(combined, 16, combined.length);
                Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
                byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
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

    public static void encryptDecryptFile(String mode, java.io.File inputFile, java.io.File outputFile, String key) {
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            keyBytes = Arrays.copyOf(keyBytes, 16);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            if (mode.equals("encrypt")) {
                byte[] iv = new byte[16];
                SecureRandom random = new SecureRandom();
                random.nextBytes(iv);
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

                try (java.io.FileInputStream fis = new java.io.FileInputStream(inputFile);
                     java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile)) {
                    fos.write(iv);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        byte[] output = cipher.update(buffer, 0, bytesRead);
                        if (output != null) fos.write(output);
                    }
                    byte[] outputBytes = cipher.doFinal();
                    if (outputBytes != null) fos.write(outputBytes);
                }
            } else if (mode.equals("decrypt")) {
                try (java.io.FileInputStream fis = new java.io.FileInputStream(inputFile);
                     java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile)) {
                    byte[] iv = new byte[16];
                    if (fis.read(iv) != 16) throw new RuntimeException("Invalid IV in file");
                    IvParameterSpec ivSpec = new IvParameterSpec(iv);
                    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        byte[] output = cipher.update(buffer, 0, bytesRead);
                        if (output != null) fos.write(output);
                    }
                    byte[] outputBytes = cipher.doFinal();
                    if (outputBytes != null) fos.write(outputBytes);
                }
            } else {
                throw new RuntimeException("Invalid mode. Please use 'encrypt' or 'decrypt'");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in File Encryption/Decryption Process", e);
        }
    }

}
