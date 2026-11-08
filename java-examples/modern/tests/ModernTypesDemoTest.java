import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Distinguishes a null selector from a nullable component in an unconditional record pattern. */
public final class ModernTypesDemoTest {
    @Test public void testSuccessAndFailureBoundaries() {
        assertEquals("value=0", ModernTypesDemo.describe(new ModernTypesDemo.Success(0)));
        assertEquals("value=2147483647", ModernTypesDemo.describe(new ModernTypesDemo.Success(Integer.MAX_VALUE)));
        assertEquals("error=missing", ModernTypesDemo.describe(new ModernTypesDemo.Failure("missing")));
        assertEquals("error=", ModernTypesDemo.describe(new ModernTypesDemo.Failure("")));
    }
    @Test public void testNullSelectorDiffersFromNullComponent() {
        assertThrows(NullPointerException.class, () -> ModernTypesDemo.describe(null));
        assertEquals("error=null", ModernTypesDemo.describe(new ModernTypesDemo.Failure(null)));
    }
    @Test public void testNegativeSuccessRejectedBeforeDescription() {
        assertThrows(IllegalArgumentException.class, () -> new ModernTypesDemo.Success(-1));
    }
}
