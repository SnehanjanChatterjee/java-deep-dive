import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/** Choose explicit precision, rounding and temporal semantics. @author Snehanjan Chatterjee */
public final class DecimalAndTime {

    /** Runs the worked example. */
    public static void main(final String[] args) throws Exception {
        final BigDecimal third = new BigDecimal("1").divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
        System.out.println(third);
        final Clock clock = Clock.fixed(Instant.parse("2025-01-02T00:00:00Z"), ZoneOffset.UTC);
        System.out.println(LocalDate.now(clock));
    }
}
