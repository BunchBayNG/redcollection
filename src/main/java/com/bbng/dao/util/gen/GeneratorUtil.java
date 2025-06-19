package com.bbng.dao.util.gen;

import java.time.LocalDate;
import java.util.Random;

import static java.lang.String.format;


public class GeneratorUtil {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstubwxyz1234567890";
    private static final Random random = new Random();

    public static String generateStandardId(Random secureRandom) {
        final String random = ensureLength(Integer.toString(secureRandom.nextInt(10000)), 4, '0');
        final LocalDate today = LocalDate.now();
        String dayOfTheYear = ensureLength(Integer.toString(today.getDayOfYear()), 3, '0');
        return format("%s%d%s", random, today.getYear() - 2000, dayOfTheYear);
    }

    public static String ensureLength(String token, int length, char prefix) {
        final int suffixLength = token.length();

        if (suffixLength > length) {
            throw new IllegalArgumentException(format("Suffix length should be less than or" +
                    " equal to %d. Suffix size: %d", length, suffixLength));
        }
        final StringBuilder sb = new StringBuilder();
        int padSize = length - suffixLength;
        for (int i = 0; i < padSize; i++) {
            sb.append(prefix);
        }
        return sb.append(token).toString();
    }

    public static String generatePassword(int length) {
        StringBuilder passwordHolder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char nextToken = characters.charAt(random.nextInt(characters.length()));
            passwordHolder.append(nextToken);
        }
        return passwordHolder.toString();
    }
}
