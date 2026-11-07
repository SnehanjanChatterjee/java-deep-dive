import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Checks the observable result and fallback invocation count in all four cases. @author Snehanjan Chatterjee */
public final class OptionalFallbackLabTest {
    @Test public void testPresentEagerAndLazy() {
        final OptionalFallbackLab.Observation eager = OptionalFallbackLab.observe(true, false);
        final OptionalFallbackLab.Observation lazy = OptionalFallbackLab.observe(true, true);
        assertEquals("present", eager.value()); assertEquals(1, eager.calls());
        assertEquals("present", lazy.value()); assertEquals(0, lazy.calls());
    }
    @Test public void testEmptyEagerAndLazy() {
        final OptionalFallbackLab.Observation eager = OptionalFallbackLab.observe(false, false);
        final OptionalFallbackLab.Observation lazy = OptionalFallbackLab.observe(false, true);
        assertEquals("fallback", eager.value()); assertEquals(1, eager.calls());
        assertEquals("fallback", lazy.value()); assertEquals(1, lazy.calls());
    }
}
