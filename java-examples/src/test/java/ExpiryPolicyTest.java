import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Checks explicit regional dates, DST and exact expiry without sleeps. @author Snehanjan Chatterjee */
public final class ExpiryPolicyTest {
    @Test public void testNormalAndDifferentRegionalDates() {
        final Clock clock = Clock.fixed(Instant.parse("2024-01-01T23:30:00Z"), ZoneOffset.UTC);
        assertEquals(Instant.parse("2024-01-02T00:00:00Z"), ExpiryPolicy.nextLocalDateStart(clock, ZoneOffset.UTC));
        assertEquals(Instant.parse("2024-01-02T18:30:00Z"), ExpiryPolicy.nextLocalDateStart(clock, ZoneId.of("Asia/Kolkata")));
    }
    @Test public void testSpringTransitionAndExactBoundary() {
        final Clock clock = Clock.fixed(Instant.parse("2024-03-30T23:00:00Z"), ZoneOffset.UTC);
        final Instant deadline = ExpiryPolicy.nextLocalDateStart(clock, ZoneId.of("Europe/Paris"));
        assertEquals(Instant.parse("2024-03-31T22:00:00Z"), deadline);
        assertFalse(ExpiryPolicy.isExpired(deadline, Clock.fixed(deadline.minusNanos(1), ZoneOffset.UTC)));
        assertTrue(ExpiryPolicy.isExpired(deadline, Clock.fixed(deadline, ZoneOffset.UTC)));
        assertTrue(ExpiryPolicy.isExpired(deadline, Clock.fixed(deadline.plusNanos(1), ZoneOffset.UTC)));
    }
    @Test public void testNullInputs() {
        final Clock clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);
        assertThrows(NullPointerException.class, () -> ExpiryPolicy.nextLocalDateStart(null, ZoneOffset.UTC));
        assertThrows(NullPointerException.class, () -> ExpiryPolicy.nextLocalDateStart(clock, null));
        assertThrows(NullPointerException.class, () -> ExpiryPolicy.isExpired(null, clock));
        assertThrows(NullPointerException.class, () -> ExpiryPolicy.isExpired(Instant.EPOCH, null));
    }
}
