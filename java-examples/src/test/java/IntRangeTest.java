import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Checks exhaustion, independent cursors and integer boundary progress. @author Snehanjan Chatterjee */
public final class IntRangeTest {
    @Test public void testTraversalAndIndependentState() {
        final IntRange range = new IntRange(2, 5);
        final List<Integer> values = new ArrayList<>();
        for (final int value : range) { values.add(value); }
        assertEquals(Arrays.asList(2, 3, 4), values);
        final Iterator<Integer> first = range.iterator();
        assertTrue(first.hasNext()); assertTrue(first.hasNext());
        assertEquals(Integer.valueOf(2), first.next());
        assertEquals(Integer.valueOf(2), range.iterator().next());
        final List<Integer> remaining = new ArrayList<>(); first.forEachRemaining(remaining::add);
        assertEquals(Arrays.asList(3, 4), remaining);
        assertFalse(first.hasNext());
        assertThrows(NoSuchElementException.class, first::next);
        assertThrows(UnsupportedOperationException.class, first::remove);
    }
    @Test public void testEmptyInvalidAndMaximumBoundary() {
        final Iterator<Integer> empty = new IntRange(0, 0).iterator();
        assertFalse(empty.hasNext()); assertThrows(NoSuchElementException.class, empty::next);
        assertThrows(IllegalArgumentException.class, () -> new IntRange(2, 1));
        final Iterator<Integer> maximum = new IntRange(Integer.MAX_VALUE - 1, Integer.MAX_VALUE).iterator();
        assertEquals(Integer.valueOf(Integer.MAX_VALUE - 1), maximum.next()); assertFalse(maximum.hasNext());
    }
}
