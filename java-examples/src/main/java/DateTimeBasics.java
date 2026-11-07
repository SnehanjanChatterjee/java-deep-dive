import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Objects;

/** Creates, parses and changes immutable calendar and timeline values. @author Snehanjan Chatterjee */
public final class DateTimeBasics {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.ROOT)
            .withResolverStyle(ResolverStyle.STRICT);
    /** Parses a real ISO calendar date, rejecting normalization of invalid input. */
    public static LocalDate parseDate(final String text) { return LocalDate.parse(Objects.requireNonNull(text, "text"), DATE); }
    /** Separates calendar arithmetic, formatting and elapsed timeline arithmetic. */
    public static void main(final String[] args) {
        final LocalDate original = parseDate("2024-02-28");
        System.out.println(original.plusDays(1));
        System.out.println(original);
        System.out.println(LocalDate.of(2024, 1, 31).plusMonths(1));
        System.out.println(DATE.format(original));
        final Instant event = Instant.parse("2024-02-28T12:00:00Z");
        System.out.println(event.plus(Duration.ofMinutes(90)));
    }
}
