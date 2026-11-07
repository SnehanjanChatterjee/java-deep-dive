package course.app;

import course.provider.LowerFormatter;
import course.provider.UpperFormatter;
import course.spi.Formatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

/** Checks capability selection rather than relying on provider iteration order. @author Snehanjan Chatterjee */
public final class DiscoverTest {
    @Test public void testSelectByCapabilityInEitherOrder() {
        final Formatter upper = new UpperFormatter();
        final Formatter lower = new LowerFormatter();
        assertSame(upper, Discover.select(Arrays.asList(lower, upper), "upper"));
        assertSame(upper, Discover.select(Arrays.asList(upper, lower), "upper"));
        assertEquals("JAVA", upper.format("Java"));
        assertEquals("java", lower.format("Java"));
    }
    @Test public void testRejectAbsentAndAmbiguousCapabilities() {
        assertThrows(NoSuchElementException.class, () -> Discover.select(Collections.<Formatter>emptyList(), "upper"));
        assertThrows(NoSuchElementException.class, () -> Discover.select(Arrays.<Formatter>asList(new LowerFormatter()), "upper"));
        assertThrows(IllegalStateException.class, () -> Discover.select(
                Arrays.<Formatter>asList(new UpperFormatter(), new UpperFormatter()), "upper"));
    }
    @Test public void testRejectNullContracts() {
        assertThrows(NullPointerException.class, () -> Discover.select(null, "upper"));
        assertThrows(NullPointerException.class, () -> Discover.select(Collections.<Formatter>emptyList(), null));
        assertThrows(NullPointerException.class, () -> Discover.select(Arrays.<Formatter>asList((Formatter) null), "upper"));
        final Formatter invalid = new Formatter() {
            @Override public String id() { return null; }
            @Override public String format(final String value) { return value; }
        };
        assertThrows(NullPointerException.class, () -> Discover.select(Arrays.asList(invalid), "upper"));
    }
}
