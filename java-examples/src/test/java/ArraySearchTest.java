import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks duplicate, absent and boundary insertion positions. @author Snehanjan Chatterjee */
public final class ArraySearchTest {
    @Test public void testEmptyDuplicatesAndAbsentTargets() {
        assertEquals(0, ArraySearch.lowerBound(new int[0], 4));
        final int[] values = {2, 4, 4, 7};
        assertEquals(0, ArraySearch.lowerBound(values, 1));
        assertEquals(0, ArraySearch.lowerBound(values, 2));
        assertEquals(1, ArraySearch.lowerBound(values, 4));
        assertEquals(3, ArraySearch.lowerBound(values, 5));
        assertEquals(4, ArraySearch.lowerBound(values, 8));
        assertEquals(0, ArraySearch.lowerBound(new int[] {4, 4, 4}, 4));
    }
    @Test public void testExtremeValuesAndNull() {
        final int[] values = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE};
        assertEquals(0, ArraySearch.lowerBound(values, Integer.MIN_VALUE));
        assertEquals(2, ArraySearch.lowerBound(values, Integer.MAX_VALUE));
        assertThrows(NullPointerException.class, () -> ArraySearch.lowerBound(null, 0));
    }
}
