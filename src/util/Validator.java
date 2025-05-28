package util;

import model.Priority;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class Validator {

    public static boolean isValidUUID(String input) {
        try {
            UUID.fromString(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isValidDate(String input) {
        try {
            LocalDate.parse(input);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidPriority(String input) {
        try {
            Priority.valueOf(input.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isValidIndex(String input, int listSize) {
        try {
            int i = Integer.parseInt(input);
            return i > 0 && i <= listSize;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }
}
