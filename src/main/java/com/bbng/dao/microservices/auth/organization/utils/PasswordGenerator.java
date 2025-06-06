package com.bbng.dao.microservices.auth.organization.utils;

import java.security.SecureRandom;

public class PasswordGenerator {

    // Characters to include in the password
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = "@#$%^&+=!-_";
    private static final String ALL_CHARS = LOWERCASE + UPPERCASE + DIGITS + SPECIALS;

    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword() {
        StringBuilder password = new StringBuilder();

        // Ensure the password has at least one of each required character
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIALS.charAt(random.nextInt(SPECIALS.length())));

        // Fill the rest of the password to meet the minimum length (8 characters)
        int remainingLength = 8 + random.nextInt(13) - 4; // Ensure a length between 8 and 20
        for (int i = 0; i < remainingLength; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        // Shuffle the characters in the password for randomness
        return shuffleString(password.toString());
    }

    // Helper method to shuffle the characters in a string
    private static String shuffleString(String input) {
        StringBuilder result = new StringBuilder(input.length());
        int[] indices = random.ints(0, input.length())
                .distinct()
                .limit(input.length())
                .toArray();
        for (int index : indices) {
            result.append(input.charAt(index));
        }
        return result.toString();
    }


    public static void main(String[] args) {
        System.out.println("Generated Password: " + generatePassword());
    }
}

