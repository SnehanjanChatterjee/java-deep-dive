import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Checks primary ordering, tie-breaking and extreme integers. @author Snehanjan Chatterjee */
public final class CollectionOrderingTest {
    @Test public void testNaturalAndExternalOrders() {
        final CollectionOrdering.Score a = new CollectionOrdering.Score("A", 20);
        final CollectionOrdering.Score b = new CollectionOrdering.Score("B", 10);
        final CollectionOrdering.ByPointsThenId order = new CollectionOrdering.ByPointsThenId();
        assertTrue(a.compareTo(b) < 0);
        assertTrue(order.compare(a, b) > 0);
        assertTrue(order.compare(new CollectionOrdering.Score("A", 10), b) < 0);
        assertEquals(0, order.compare(b, b));
        assertEquals("A:20", a.toString());
        assertEquals(a, new CollectionOrdering.Score("A", 20));
        assertEquals(a.hashCode(), new CollectionOrdering.Score("A", 20).hashCode());
        assertEquals(0, a.compareTo(new CollectionOrdering.Score("A", 20)));
        assertTrue(a.compareTo(new CollectionOrdering.Score("A", 10)) > 0);
        assertFalse(a.equals(b)); assertFalse(a.equals(null)); assertFalse(a.equals("A"));
        assertEquals(a, a);
    }
    @Test public void testExtremeAndNullInputs() {
        final CollectionOrdering.ByPointsThenId order = new CollectionOrdering.ByPointsThenId();
        assertTrue(order.compare(new CollectionOrdering.Score("A", Integer.MIN_VALUE),
                new CollectionOrdering.Score("B", Integer.MAX_VALUE)) < 0);
        assertThrows(NullPointerException.class, () -> new CollectionOrdering.Score(null, 1));
    }
}
