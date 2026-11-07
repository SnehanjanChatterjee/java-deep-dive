import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Checks coordinated membership/order updates and endpoint reuse. @author Snehanjan Chatterjee */
public final class SimpleLinkedHashSetTest {
    @Test public void testDuplicateAndReinsertion() {
        final SimpleLinkedHashSet<String> set = new SimpleLinkedHashSet<>();
        assertTrue(set.add("B")); assertTrue(set.add("A")); assertTrue(set.add("C"));
        assertFalse(set.add(new String("A")));
        assertEquals(Arrays.asList("B", "A", "C"), set.asList());
        assertTrue(set.remove("A")); assertTrue(set.add("A"));
        assertEquals(Arrays.asList("B", "C", "A"), set.asList());
        assertEquals(3, set.size());
        assertFalse(set.remove("missing"));
    }
    @Test public void testEndpointsNullAndCollisions() {
        final SimpleLinkedHashSet<String> set = new SimpleLinkedHashSet<>();
        set.add("Aa"); set.add("BB"); set.add(null);
        assertTrue(set.remove("Aa")); assertTrue(set.contains("BB"));
        assertTrue(set.remove(null)); assertFalse(set.contains(null));
        assertTrue(set.remove("BB"));
        assertEquals(Collections.emptyList(), set.asList());
        set.add(null);
        assertTrue(set.contains(null));
        assertFalse(set.add(null));
        assertEquals(Arrays.asList((String) null), set.asList());
        set.asList().clear();
        assertEquals(1, set.size());
    }
}
