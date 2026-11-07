import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/** Exercises logical bounds, shifts, nulls and growth. @author Snehanjan Chatterjee */
public final class SimpleArrayListTest {
    @Test public void testInsertionRemovalAndGrowth() {
        final SimpleArrayList<Integer> values = new SimpleArrayList<>();
        for (int i = 0; i < 100; i++) { values.add(i); }
        values.add(0, -1); values.add(51, 999); values.add(values.size(), 100);
        assertEquals(Integer.valueOf(-1), values.remove(0));
        assertEquals(Integer.valueOf(999), values.remove(50));
        assertEquals(Integer.valueOf(100), values.remove(values.size() - 1));
        assertEquals(100, values.size());
        for (int i = 0; i < 100; i++) { assertEquals(Integer.valueOf(i), values.get(i)); }
    }
    @Test public void testNullReplacementClearAndReuse() {
        final SimpleArrayList<String> values = new SimpleArrayList<>();
        values.add(null); assertNull(values.get(0)); assertNull(values.set(0, "new"));
        assertEquals("new", values.set(0, null)); assertNull(values.remove(0));
        values.add("retained"); values.clear(); assertEquals(0, values.size());
        values.add("reused"); assertEquals("reused", values.get(0));
    }
    @Test public void testInvalidPositionsDoNotMutate() {
        final SimpleArrayList<Integer> values = new SimpleArrayList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> values.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> values.add(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> values.add(1, 1));
        values.add(7);
        assertThrows(IndexOutOfBoundsException.class, () -> values.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> values.set(-1, 9));
        assertEquals(1, values.size()); assertEquals(Integer.valueOf(7), values.get(0));
    }
    @Test public void testBulkCompactionAndFailure() {
        final SimpleArrayList<Integer> values = new SimpleArrayList<>();
        values.add(1); values.add(2); values.add(3); values.add(4);
        assertEquals(2, values.removeAllMatching(value -> value % 2 == 0));
        assertEquals(Integer.valueOf(1), values.get(0)); assertEquals(Integer.valueOf(3), values.get(1));
        assertThrows(IllegalStateException.class, () -> values.removeAllMatching(value -> {
            if (value == 3) { throw new IllegalStateException("deliberate rule failure"); }
            return true;
        }));
        assertEquals(2, values.size()); assertEquals(Integer.valueOf(1), values.get(0));
        assertEquals(0, values.removeAllMatching(value -> false));
        assertEquals(2, values.removeAllMatching(value -> true));
        assertEquals(0, values.removeAllMatching(value -> true));
        assertThrows(NullPointerException.class, () -> values.removeAllMatching(null));
        values.add(null); assertEquals(1, values.removeAllMatching(value -> value == null));
    }
}
