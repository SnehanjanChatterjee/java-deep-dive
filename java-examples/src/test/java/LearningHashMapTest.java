/*
 * Java Deep Dive educational examples.
 * Author: Snehanjan Chatterjee.
 */
import java.util.AbstractMap;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Exercises the teaching map's public behavior and backed views. @author Snehanjan Chatterjee */
public final class LearningHashMapTest {
    @Test
    public void testEmptyAndMissingMappings() {
        final Map<String, Integer> map = new LearningHashMap<>();
        assertTrue(map.isEmpty());
        assertNull(map.get("missing"));
        assertFalse(map.containsKey("missing"));
        assertNull(map.remove("missing"));
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testInsertReplaceAndReturnValues() {
        final Map<String, Integer> map = new LearningHashMap<>();
        assertNull(map.put("one", 1));
        assertEquals(Integer.valueOf(1), map.put(new String("one"), 2));
        assertEquals(1, map.size());
        assertEquals(Integer.valueOf(2), map.get("one"));
        assertEquals(Integer.valueOf(2), map.remove("one"));
        assertTrue(map.isEmpty());
    }

    @Test
    public void testNullKeyAndNullValue() {
        final Map<String, Integer> map = new LearningHashMap<>();
        map.put(null, 1);
        map.put("null-value", null);
        assertEquals(Integer.valueOf(1), map.get(null));
        assertTrue(map.containsKey("null-value"));
        assertNull(map.getOrDefault("null-value", 99));
        assertEquals(Integer.valueOf(99), map.getOrDefault("missing", 99));
        assertEquals(Integer.valueOf(1), map.remove(null));
        assertTrue(map.remove("null-value", null));
        assertTrue(map.isEmpty());
    }

    @Test
    public void testHashCollisionAndChainRemoval() {
        final Map<Key, Integer> map = new LearningHashMap<>(16);
        map.put(new Key(1, 7), 10);
        map.put(new Key(2, 7), 20);
        map.put(new Key(3, 7), 30);
        assertEquals(Integer.valueOf(20), map.get(new Key(2, 7)));
        assertNull(map.get(new Key(4, 7)));
        assertNull(map.remove(new Key(4, 7)));
        assertEquals(Integer.valueOf(20), map.remove(new Key(2, 7))); // middle
        assertEquals(Integer.valueOf(10), map.remove(new Key(1, 7))); // tail
        assertEquals(Integer.valueOf(30), map.remove(new Key(3, 7))); // head
        assertTrue(map.isEmpty());
    }

    @Test
    public void testNegativeAndMinimumHashes() {
        final Map<Key, Integer> map = new LearningHashMap<>(3);
        map.put(new Key(1, -1), 10);
        map.put(new Key(2, Integer.MIN_VALUE), 20);
        assertEquals(Integer.valueOf(10), map.get(new Key(1, -1)));
        assertEquals(Integer.valueOf(20), map.get(new Key(2, Integer.MIN_VALUE)));
    }

    @Test
    public void testMultipleResizesPreserveMappings() {
        final Map<Integer, Integer> map = new LearningHashMap<>(1);
        for (int i = 0; i < 2048; i++) {
            map.put(i, i * 3);
        }
        assertEquals(2048, map.size());
        for (int i = 0; i < 2048; i++) {
            assertEquals(Integer.valueOf(i * 3), map.get(i));
        }
    }

    @Test
    public void testCollisionChainsSurviveResize() {
        final Map<Key, Integer> map = new LearningHashMap<>(1);
        for (int i = 0; i < 50; i++) {
            map.put(new Key(i, 5), i);
        }
        for (int i = 0; i < 50; i++) {
            assertEquals(Integer.valueOf(i), map.get(new Key(i, 5)));
        }
        assertEquals(50, map.size());
    }

    @Test
    public void testInvalidAndMaximumCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new LearningHashMap<>(0));
        assertThrows(IllegalArgumentException.class, () -> new LearningHashMap<>(-1));
        assertThrows(IllegalArgumentException.class, () -> new LearningHashMap<>((1 << 20) + 1));
        final Map<Integer, Integer> map = new LearningHashMap<>(1 << 20);
        map.put(1, 2);
        assertEquals(Integer.valueOf(2), map.get(1));
    }

    @Test
    public void testIteratorVisitsEntriesOnce() {
        final Map<Integer, Integer> map = new LearningHashMap<>();
        map.put(1, 10);
        map.put(17, 20);
        map.put(33, 30);
        map.put(2, 40);
        final Set<Integer> seen = new HashSet<>();
        for (final Map.Entry<Integer, Integer> entry : map.entrySet()) {
            assertTrue(seen.add(entry.getKey()));
            assertEquals(map.get(entry.getKey()), entry.getValue());
        }
        assertEquals(map.keySet(), seen);
    }

    @Test
    public void testIteratorExhaustionAndIllegalRemove() {
        final Map<String, Integer> map = new LearningHashMap<>();
        final Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    public void testIteratorRemoveKeepsTraversalValid() {
        final Map<Integer, Integer> map = new LearningHashMap<>();
        map.put(1, 10);
        map.put(17, 20);
        map.put(33, 30);
        final Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
            assertThrows(IllegalStateException.class, iterator::remove);
        }
        assertTrue(map.isEmpty());
    }

    @Test
    public void testFailFastNextAndRemove() {
        final Map<String, Integer> map = new LearningHashMap<>();
        map.put("one", 1);
        final Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        iterator.next();
        map.put("two", 2);
        assertThrows(ConcurrentModificationException.class, iterator::next);
        assertThrows(ConcurrentModificationException.class, iterator::remove);
    }

    @Test
    public void testReplacementDoesNotInvalidateIterator() {
        final Map<String, Integer> map = new LearningHashMap<>();
        map.put("one", 1);
        final Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        map.put("one", 2);
        assertEquals(Integer.valueOf(2), iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testBackedEntryAndViewOperations() {
        final Map<String, Integer> map = new LearningHashMap<>();
        map.put("one", 1);
        final Map.Entry<String, Integer> entry = map.entrySet().iterator().next();
        assertEquals(Integer.valueOf(1), entry.setValue(2));
        assertEquals(Integer.valueOf(2), map.get("one"));
        assertTrue(map.keySet().remove("one"));
        assertTrue(map.isEmpty());
        map.put("two", 2);
        assertTrue(map.values().remove(2));
        map.put("three", 3);
        assertTrue(map.entrySet().remove(new AbstractMap.SimpleEntry<>("three", 3)));
        map.put("four", 4);
        map.entrySet().clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testEntryEqualityAndHashCode() {
        final Map<String, Integer> map = new LearningHashMap<>();
        map.put(null, null);
        final Map.Entry<String, Integer> entry = map.entrySet().iterator().next();
        assertEquals(new AbstractMap.SimpleEntry<>(null, null), entry);
        assertEquals(0, entry.hashCode());
        assertNotEquals("not an entry", entry);
        assertNotEquals(new AbstractMap.SimpleEntry<>("other", null), entry);
        assertNotEquals(new AbstractMap.SimpleEntry<>(null, 1), entry);
        entry.setValue(3);
        assertEquals(3, entry.hashCode());
    }

    @Test
    public void testClearInvalidatesIteratorAndMapCanBeReused() {
        final Map<String, Integer> map = new LearningHashMap<>();
        map.put("one", 1);
        final Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        map.clear();
        assertThrows(ConcurrentModificationException.class, iterator::next);
        assertNull(map.get("one"));
        map.put("two", 2);
        assertEquals(Integer.valueOf(2), map.get("two"));
    }

    @Test
    public void testInheritedMapDefaultOperations() {
        final Map<String, Integer> map = new LearningHashMap<>();
        assertNull(map.putIfAbsent("one", 1));
        assertEquals(Integer.valueOf(1), map.putIfAbsent("one", 2));
        assertEquals(Integer.valueOf(4), map.merge("one", 3, Integer::sum));
        assertEquals(Integer.valueOf(7), map.compute("one", (key, value) -> value + 3));
        assertEquals(Integer.valueOf(2), map.computeIfAbsent("two", key -> 2));
        assertFalse(map.remove("two", 3));
        assertTrue(map.replace("two", 2, 5));
        map.replaceAll((key, value) -> value * 2);
        assertEquals(Integer.valueOf(10), map.get("two"));
    }

    @Test
    public void testRandomizedParityAgainstJdkHashMap() {
        final Map<Key, Integer> actual = new LearningHashMap<>(2);
        final Map<Key, Integer> expected = new HashMap<>();
        final Random random = new Random(2026);
        for (int i = 0; i < 5000; i++) {
            final int id = random.nextInt(80);
            final Key key = id == 0 ? null : new Key(id, id % 4);
            final Integer value = random.nextInt(5) == 0 ? null : random.nextInt();
            switch (random.nextInt(5)) {
                case 0:
                case 1:
                    assertEquals(expected.put(key, value), actual.put(key, value));
                    break;
                case 2:
                    assertEquals(expected.remove(key), actual.remove(key));
                    break;
                case 3:
                    assertEquals(expected.get(key), actual.get(key));
                    break;
                default:
                    assertEquals(expected.containsKey(key), actual.containsKey(key));
                    break;
            }
            assertEquals(expected, actual);
            assertEquals(expected.hashCode(), actual.hashCode());
        }
    }

    private static final class Key {
        private final int id;
        private final int hash;
        private Key(final int id, final int hash) { this.id = id; this.hash = hash; }
        @Override public int hashCode() { return hash; }
        @Override public boolean equals(final Object other) {
            return other instanceof Key && ((Key) other).id == id;
        }
    }
}
