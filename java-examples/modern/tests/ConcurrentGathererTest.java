import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks the stage's declared result, admission and null policies. @author Snehanjan Chatterjee */
public final class ConcurrentGathererTest {
    @Test public void testPreservesInputEncounterOrder() {
        assertEquals(Arrays.asList("v3", "v1", "v2"), ConcurrentGatherer.labels(Arrays.asList(3, 1, 2), 2));
    }
    @Test public void testAcceptsEmptyInput() {
        assertEquals(Collections.emptyList(), ConcurrentGatherer.labels(Collections.emptyList(), 1));
    }
    @Test public void testRejectsZeroConcurrency() {
        assertThrows(IllegalArgumentException.class, () -> ConcurrentGatherer.labels(Collections.emptyList(), 0));
    }
    @Test public void testRejectsNegativeConcurrency() {
        assertThrows(IllegalArgumentException.class, () -> ConcurrentGatherer.labels(Collections.emptyList(), -1));
    }
    @Test public void testRejectsNullInput() {
        assertThrows(NullPointerException.class, () -> ConcurrentGatherer.labels(null, 2));
    }
    @Test public void testPropagatesNullElementFailure() {
        assertThrows(RuntimeException.class, () -> ConcurrentGatherer.labels(Arrays.asList(1, null, 2), 2));
    }
}
