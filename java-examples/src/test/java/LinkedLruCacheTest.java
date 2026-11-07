import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/** Tests the joint index/recency invariant through observable eviction. @author Snehanjan Chatterjee */
public final class LinkedLruCacheTest {
    @Test public void testAccessAndReplacementChangeRecency() {
        final LinkedLruCache<String, Integer> cache = new LinkedLruCache<>(2);
        cache.put("A", 1); cache.put("B", 2); assertEquals(Integer.valueOf(1), cache.get("A"));
        cache.put("C", 3); assertNull(cache.get("B"));
        cache.put("A", 4); cache.put("D", 5); assertNull(cache.get("C"));
        assertEquals(Integer.valueOf(4), cache.get("A")); assertEquals(2, cache.size());
    }
    @Test public void testCapacityOneAndMissingAccess() {
        final LinkedLruCache<String, Integer> cache = new LinkedLruCache<>(1);
        cache.put("A", 1); assertNull(cache.get("missing")); cache.put("B", 2);
        assertNull(cache.get("A")); assertEquals(Integer.valueOf(2), cache.get("B"));
        cache.put("B", 3); assertEquals(1, cache.size()); assertEquals(Integer.valueOf(3), cache.get("B"));
    }
    @Test public void testRejectedInputsLeaveStateValid() {
        assertThrows(IllegalArgumentException.class, () -> new LinkedLruCache<>(0));
        final LinkedLruCache<String, Integer> cache = new LinkedLruCache<>(1);
        assertThrows(NullPointerException.class, () -> cache.put(null, 1));
        assertThrows(NullPointerException.class, () -> cache.put("A", null));
        assertThrows(NullPointerException.class, () -> cache.get(null));
        assertEquals(0, cache.size());
    }
}
