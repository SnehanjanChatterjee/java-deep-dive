import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks empty, ordinary and invalid sizes independently of timing. @author Snehanjan Chatterjee */
public final class OperationCountsTest {
    @Test public void testEmptyAndSingleWork() {
        assertEquals(0, OperationCounts.linear(0)); assertEquals(0, OperationCounts.quadratic(0));
        assertEquals(1, OperationCounts.linear(1)); assertEquals(1, OperationCounts.quadratic(1));
    }
    @Test public void testDifferentGrowth() { assertEquals(4, OperationCounts.linear(4)); assertEquals(16, OperationCounts.quadratic(4)); }
    @Test public void testNegativeSizes() {
        assertThrows(IllegalArgumentException.class, () -> OperationCounts.linear(-1));
        assertThrows(IllegalArgumentException.class, () -> OperationCounts.quadratic(-1));
    }
}
