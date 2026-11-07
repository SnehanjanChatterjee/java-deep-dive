import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks range partition coverage, identities and input constraints. @author Snehanjan Chatterjee */
public final class ForkJoinRangeSumTest {
    @Test public void testRangesAndThresholds() {
        final int[] values = {1, 2, -3, 4, 5, -6, 7};
        assertEquals(10L, ForkJoinRangeSum.sum(values, 1));
        assertEquals(10L, ForkJoinRangeSum.sum(values, 2));
        assertEquals(10L, ForkJoinRangeSum.sum(values, 100));
        assertEquals(0L, ForkJoinRangeSum.sum(new int[0], 1));
        assertEquals(4294967294L, ForkJoinRangeSum.sum(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE}, 1));
    }
    @Test public void testInvalidInputs() {
        assertThrows(NullPointerException.class, () -> ForkJoinRangeSum.sum(null, 1));
        assertThrows(IllegalArgumentException.class, () -> ForkJoinRangeSum.sum(new int[0], 0));
    }
}
