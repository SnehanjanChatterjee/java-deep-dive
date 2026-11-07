import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/** Verifies equality and both defensive-copy boundaries. @author Snehanjan Chatterjee */
public final class ImmutableScheduleTest {
    @Test public void testDetachedInputAndOutput() {
        final Date input = new Date(1L);
        final ImmutableSchedule schedule = new ImmutableSchedule(Collections.singletonList(input));
        input.setTime(2L); schedule.dates().get(0).setTime(3L);
        assertEquals(1L, schedule.dates().get(0).getTime());
        assertThrows(UnsupportedOperationException.class, () -> schedule.dates().add(new Date()));
    }
    @Test public void testValueEqualityAndRejections() {
        final ImmutableSchedule left = new ImmutableSchedule(Collections.singletonList(new Date(1L)));
        final ImmutableSchedule right = new ImmutableSchedule(Collections.singletonList(new Date(1L)));
        assertEquals(left, right); assertEquals(left.hashCode(), right.hashCode()); assertEquals(left, left);
        assertFalse(left.equals(null)); assertFalse(left.equals("other"));
        assertFalse(left.equals(new ImmutableSchedule(Collections.singletonList(new Date(2L)))));
        assertThrows(NullPointerException.class, () -> new ImmutableSchedule(null));
        assertThrows(NullPointerException.class, () -> new ImmutableSchedule(Arrays.asList(new Date(1L), null)));
    }
}
