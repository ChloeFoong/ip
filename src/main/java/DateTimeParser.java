import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateTimeParser {
    private static final DateTimeFormatter DAY_MONTH_YEAR_TIME =
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    private DateTimeParser() {}

    public static LocalDateTime parse(String value) {
        String input = value.trim();
        try {
            if (input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDate.parse(input).atStartOfDay();
            }
            return LocalDateTime.parse(input, DAY_MONTH_YEAR_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date/time. Use yyyy-MM-dd or d/M/yyyy HHmm.", e);
        }
    }

    public static String formatForDisplay(LocalDateTime value) {
        return value.format(DISPLAY_FORMAT);
    }

    public static String formatForStorage(LocalDateTime value) {
        return value.toString();
    }
}
