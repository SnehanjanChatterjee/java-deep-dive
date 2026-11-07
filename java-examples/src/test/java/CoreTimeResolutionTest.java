import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks explicit civil-time ambiguity policies. @author Snehanjan Chatterjee */
public final class CoreTimeResolutionTest {
    @Test public void testUniqueTimeAndOverlapChoices() {
        final ZoneId paris = ZoneId.of("Europe/Paris");
        final LocalDateTime unique = LocalDateTime.of(2024, 11, 1, 12, 0);
        assertEquals(unique, CoreTimeResolution.resolve(unique, paris, CoreTimeResolution.OverlapPolicy.REJECT).toLocalDateTime());
        final LocalDateTime overlap = LocalDateTime.of(2024, 10, 27, 2, 30);
        assertEquals(3600L, Duration.between(
                CoreTimeResolution.resolve(overlap, paris, CoreTimeResolution.OverlapPolicy.EARLIER_OFFSET).toInstant(),
                CoreTimeResolution.resolve(overlap, paris, CoreTimeResolution.OverlapPolicy.LATER_OFFSET).toInstant()).getSeconds());
    }
    @Test public void testGapOverlapRejectionAndNulls() {
        final ZoneId paris = ZoneId.of("Europe/Paris");
        assertThrows(DateTimeException.class, () -> CoreTimeResolution.resolve(LocalDateTime.of(2024, 3, 31, 2, 30), paris, CoreTimeResolution.OverlapPolicy.REJECT));
        assertThrows(DateTimeException.class, () -> CoreTimeResolution.resolve(LocalDateTime.of(2024, 10, 27, 2, 30), paris, CoreTimeResolution.OverlapPolicy.REJECT));
        assertThrows(NullPointerException.class, () -> CoreTimeResolution.resolve(null, paris, CoreTimeResolution.OverlapPolicy.REJECT));
        assertThrows(NullPointerException.class, () -> CoreTimeResolution.resolve(LocalDateTime.of(2024, 11, 1, 12, 0), null, CoreTimeResolution.OverlapPolicy.REJECT));
        assertThrows(NullPointerException.class, () -> CoreTimeResolution.resolve(LocalDateTime.of(2024, 11, 1, 12, 0), paris, null));
    }
}
