import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;

/** Defines expiry at the next local date boundary using an injected clock. @author Snehanjan Chatterjee */
public final class ExpiryPolicy {
    /** Resolves the first valid start of the next date in the declared region. */
    public static Instant nextLocalDateStart(final Clock clock, final ZoneId zone) {
        Objects.requireNonNull(clock, "clock");
        Objects.requireNonNull(zone, "zone");
        final LocalDate today = LocalDate.now(clock.withZone(zone));
        return today.plusDays(1).atStartOfDay(zone).toInstant();
    }
    /** Treats equality with the deadline as expired. */
    public static boolean isExpired(final Instant deadline, final Clock clock) {
        Objects.requireNonNull(deadline, "deadline");
        Objects.requireNonNull(clock, "clock");
        return !clock.instant().isBefore(deadline);
    }
    /** Demonstrates a 23-hour local date without sleeping or changing system time. */
    public static void main(final String[] args) {
        final Clock clock = Clock.fixed(Instant.parse("2024-03-30T23:00:00Z"), ZoneOffset.UTC);
        final Instant deadline = nextLocalDateStart(clock, ZoneId.of("Europe/Paris"));
        System.out.println(deadline);
        System.out.println(Duration.between(clock.instant(), deadline).toHours());
        System.out.println(isExpired(deadline, Clock.fixed(deadline, ZoneOffset.UTC)));
    }
}
