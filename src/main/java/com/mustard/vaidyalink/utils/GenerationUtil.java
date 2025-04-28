package com.mustard.vaidyalink.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerationUtil {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*-_+=";
    private static final int PASSWORD_LENGTH = 12;

    public static String generateSecurePassword() {
        SecureRandom random = new SecureRandom();
        List<Character> passwordChars = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            passwordChars.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        }
        for (int i = 0; i < 2; i++) {
            passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        passwordChars.add(UPPER.charAt(random.nextInt(UPPER.length())));

        while (passwordChars.size() < PASSWORD_LENGTH) {
            passwordChars.add(LOWER.charAt(random.nextInt(LOWER.length())));
        }

        Collections.shuffle(passwordChars, random);

        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }
        return password.toString();
    }

    public static String generateRegistrationNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder regNum = new StringBuilder();

        for (int i = 0; i < 2; i++) {
            regNum.append(UPPER.charAt(random.nextInt(UPPER.length())));
        }
        for (int i = 0; i < 6; i++) {
            regNum.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }

        return regNum.toString();
    }

}
