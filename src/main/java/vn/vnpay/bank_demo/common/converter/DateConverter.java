package vn.vnpay.bank_demo.common.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    public static LocalDate convertStringToLocalDate(String strDate, String pattern) {
        if (strDate.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(strDate, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertLocalDateToString(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return date.format(formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertLocalDateTimeToString(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return dateTime.format(formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime convertStringToLocalDateTime(String strDateTime, String pattern) {
        if (strDateTime.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(strDateTime, formatter);
        } catch (Exception e) {
            return null;
        }
    }


}
