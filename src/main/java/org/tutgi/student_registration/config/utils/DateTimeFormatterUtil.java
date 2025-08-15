package org.tutgi.student_registration.config.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeFormatterUtil {

    private static final DateTimeFormatter DEFAULT_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH);

    private static final DateTimeFormatter FULL_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm:ss", Locale.ENGLISH);

    private static final DateTimeFormatter ISO_DATE_TIME =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private DateTimeFormatterUtil() {}

    public static String formatDefault(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_FORMATTER);
    }

    public static String formatFull(LocalDateTime dateTime) {
        return dateTime.format(FULL_DATETIME_FORMATTER);
    }

    public static String formatIso(LocalDateTime dateTime) {
        return dateTime.format(ISO_DATE_TIME);
    }

    public static String formatCustom(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        return dateTime.format(customFormatter);
    }
}

