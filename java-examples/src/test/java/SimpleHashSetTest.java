import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Verifies duplicate, collision and null membership contracts. @author Snehanjan Chatterjee */
public final class SimpleHashSetTest {
    @Test public void testEqualKeysAndNull() {
        final SimpleHashSet<String> values = new SimpleHashSet<>();
        assertTrue(values.add("java")); assertFalse(values.add(new String("java")));
        assertTrue(values.add(null)); assertTrue(values.contains(null));
        assertTrue(values.remove(null)); assertFalse(values.remove(null)); assertEquals(1, values.size());
    }
    @Test public void testCollisionsAndGrowth() {
        final SimpleHashSet<String> values = new SimpleHashSet<>();
        assertTrue(values.add("Aa")); assertTrue(values.add("BB"));
        for (int i = 0; i < 100; i++) { values.add("key-" + i); }
        assertTrue(values.remove("Aa")); assertTrue(values.contains("BB"));
        assertFalse(values.contains("missing")); assertEquals(101, values.size());
    }
}
