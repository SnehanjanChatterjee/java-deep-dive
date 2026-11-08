import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Uses fixed inputs to compare calendar fields, local arithmetic and timeline amounts. @author Snehanjan Chatterjee */
public final class TemporalOperationsLab {
    /** Prints immutable field replacement, arithmetic, formatting and elapsed minutes. */
    public static void main(final String[] args) {
        final LocalDate date = LocalDate.of(2024, 1, 31);
        System.out.println("year=" + date.getYear() + ", day=" + date.getDayOfMonth());
        System.out.println("with day=" + date.withDayOfMonth(1));
        System.out.println("plus month=" + date.plusMonths(1));
        final LocalTime time = LocalTime.of(23, 45);
        System.out.println("time plus30=" + time.plusMinutes(30));
        System.out.println("datetime plus30=" + LocalDateTime.of(date, time).plusMinutes(30));
        System.out.println("formatted=" + date.format(DateTimeFormatter.BASIC_ISO_DATE));
        final Instant first = Instant.parse("2024-01-01T12:00:00Z");
        final Instant later = first.plusSeconds(3600);
        System.out.println("later=" + later);
        System.out.println("before=" + first.isBefore(later));
        System.out.println("elapsed minutes=" + Duration.between(first, later).toMinutes());
    }
}
