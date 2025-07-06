// src/main/java/org/tutgi/student_registration/config/utils/NrcUtils.java
package org.tutgi.student_registration.config.utils;

import java.util.Optional; // Add this import

public class NrcUtils {

    // Mapping for Burmese digits to English digits
    private static final java.util.Map<Character, Character> BURMESE_DIGIT_TO_ENGLISH_MAP = new java.util.HashMap<>();
    static {
        for (int i = 0; i <= 9; i++) {
            BURMESE_DIGIT_TO_ENGLISH_MAP.put((char) ('\u1040' + i), (char) ('0' + i));
        }
    }

    /**
     * Converts Burmese digits within a string to their English (ASCII) equivalents.
     * Other characters remain unchanged.
     *
     * @param input The string potentially containing Burmese digits.
     * @return A new string with Burmese digits converted to English digits.
     */
    public static String convertBurmeseDigitsToEnglish(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (BURMESE_DIGIT_TO_ENGLISH_MAP.containsKey(ch)) {
                result.append(BURMESE_DIGIT_TO_ENGLISH_MAP.get(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * Parses an NRC string into its components.
     * This method assumes the NRC string generally conforms to the expected pattern.
     * It does NOT perform full validation, only structural parsing.
     *
     * @param nrc The input NRC string (can contain Burmese or English chars/digits).
     * @return An Optional containing a String array {stateNumber, townshipCodeOrShort, nrcType, trailingNumberPart} if parsing is successful,
     * otherwise Optional.empty().
     */
    public static Optional<String[]> parseNrcComponents(String nrc) {
        if (nrc == null || nrc.trim().isEmpty()) {
            return Optional.empty();
        }

        String nrcPartToValidate = nrc;
        String trailingNumberPart = "";

        int lastParenIndex = nrc.lastIndexOf(")");
        if (lastParenIndex != -1 && lastParenIndex + 1 < nrc.length()
                && (Character.isDigit(nrc.charAt(lastParenIndex + 1)) || (nrc.charAt(lastParenIndex + 1) >= '\u1040' && nrc.charAt(lastParenIndex + 1) <= '\u1049'))) {
            nrcPartToValidate = nrc.substring(0, lastParenIndex + 1);
            trailingNumberPart = nrc.substring(lastParenIndex + 1).trim();
        }

        // The regex needs to be broad here to just parse, not validate content
        if (!nrcPartToValidate.matches("^[\\d\\u1040-\\u1049]{1,2}/[A-Z\\u1000-\\u109F]+\\([A-Z\\u1000-\\u109F]+\\)$")) {
            return Optional.empty(); // Fails basic structure
        }

        String[] parts = nrcPartToValidate.split("[/()]");
        if (parts.length != 3) {
            return Optional.empty(); // Not enough parts
        }

        return Optional.of(new String[]{parts[0], parts[1], parts[2], trailingNumberPart});
    }
}