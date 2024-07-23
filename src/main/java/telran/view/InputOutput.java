package telran.view;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
    String readString(String prompt);

    void writeString(String str);

    default void writeLine(Object o) {
        writeString(o.toString() + "\n");
    }

    default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
        T res = null;
        boolean running = false;
        do {
            String str = readString(prompt);
            running = false;
            try {
                res = mapper.apply(str);
            } catch (RuntimeException e) {
                writeLine(errorPrompt + " " + e.getMessage());
                running = true;
            }
        } while (running);
        return res;
    }

    /**
     * @param prompt
     * @param errorPrompt
     * @return Integer
     */
    default Integer readInt(String prompt, String errorPrompt) {
        // Entered string must be a number otherwise, errorPrompt with cycle
        return readObject(prompt, errorPrompt, Integer::parseInt);
    }

    default Long readLong(String prompt, String errorPrompt) {
        // Entered string must be a number otherwise, errorPrompt with cycle
        return readObject(prompt, errorPrompt, Long::parseLong);
    }

    default Double readDouble(String prompt, String errorPrompt) {
        // Entered string must be a number otherwise, errorPrompt with cycle
        return readObject(prompt, errorPrompt, Double::parseDouble);
    }

    default Double readNumberRange(String prompt, String errorPrompt, Double min, Double max) {
        // Entered string must be a number in range (min <= number < max) otherwise, errorPrompt with cycle
        return readObject(prompt, errorPrompt, s -> {
            Double value = Double.parseDouble(s);
            if (value < min) {
                throw new IllegalArgumentException("Number must be more that " + min);
            } else if (value >= max) {
                throw new IllegalArgumentException("Number must be less than " + max);
            }
            return value;
        });
    }

    default String readStringPredicate(String prompt, String errorPrompt, Predicate<String> predicate) {
        // Entered String must match a given predicate
        return readObject(prompt, errorPrompt, str -> {
            if (!predicate.test(str)) {
                throw new IllegalArgumentException("The string does not match the condition");
            }
            return str;
        });
    }

    default String readStringPredicate(String prompt, String errorPrompt, HashSet<String> options) {
        // Entered String must one out of a given options
        return readStringPredicate(prompt, errorPrompt, options::contains);
    }

    default LocalDate readIsoDate(String prompt, String errorPrompt) {
        // Entered string must be a local a LocalDate in format (yyyy-mm-dd)
        return readObject(prompt, errorPrompt, LocalDate::parse);
    }

    default LocalDate readIsoDateRange(String prompt, String errorPrompt, LocalDate from, LocalDate to) {
        // Entered string must be a local a LocalDate in format (yyyy-mm-dd) in the (after from, before to)
        return readObject(prompt, errorPrompt, str -> {
            LocalDate localDate = LocalDate.parse(str);
            if (localDate.isBefore(from)) {
                throw new IllegalArgumentException("Date must be after " + from.toString());
            } else if (localDate.isAfter(to)) {
                throw new IllegalArgumentException("Date must be before " + to.toString());
            }
            return localDate;
        });
    }
}
