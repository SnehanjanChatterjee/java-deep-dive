import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks two correct increment protocols at normal and validation boundaries. @author Snehanjan Chatterjee */
public final class CounterComparisonLabTest {
    @Test public void testCompletedCountsAndZero() throws InterruptedException {
        final CounterComparisonLab.Result result = CounterComparisonLab.count(1000);
        assertEquals(2000, result.synchronizedCount()); assertEquals(2000, result.atomicCount());
        final CounterComparisonLab.Result empty = CounterComparisonLab.count(0);
        assertEquals(0, empty.synchronizedCount()); assertEquals(0, empty.atomicCount());
    }
    @Test public void testRejectsInvalidOrOverflowingRequestedCount() {
        assertThrows(IllegalArgumentException.class, () -> CounterComparisonLab.count(-1));
        assertThrows(ArithmeticException.class, () -> CounterComparisonLab.count(Integer.MAX_VALUE));
    }
}
