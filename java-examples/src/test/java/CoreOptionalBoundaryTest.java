import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/** Tests absence separately from malformed and out-of-range input. @author Snehanjan Chatterjee */
public final class CoreOptionalBoundaryTest {
    @Test public void testPresentBoundariesAndAbsent() {
        assertEquals(Integer.valueOf(1), CoreOptionalBoundary.parsePort("1").get());
        assertEquals(Integer.valueOf(65535), CoreOptionalBoundary.parsePort("65535").get());
        assertFalse(CoreOptionalBoundary.parsePort(null).isPresent());
    }
    @Test public void testInvalidPresentValuesAreNotEmpty() {
        assertThrows(NumberFormatException.class, () -> CoreOptionalBoundary.parsePort(""));
        assertThrows(NumberFormatException.class, () -> CoreOptionalBoundary.parsePort("text"));
        assertThrows(IllegalArgumentException.class, () -> CoreOptionalBoundary.parsePort("0"));
        assertThrows(IllegalArgumentException.class, () -> CoreOptionalBoundary.parsePort("65536"));
    }
}
