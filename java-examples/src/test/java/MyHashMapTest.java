import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/** Checks the one-file interview exercise. @author Snehanjan Chatterjee */
public final class MyHashMapTest {
    @Test
    public void testEmptyAndMissing() {
        final MyHashMap<String, Integer> map = new MyHashMap<>();
        assertEquals(0, map.size());
        assertNull(map.get("absent"));
        assertNull(map.remove("absent"));
        assertFalse(map.containsKey("absent"));
    }
    @Test
    public void testEqualDistinctKeyReplaces() {
        final MyHashMap<String, Integer> map = new MyHashMap<>();
        assertNull(map.put("one", 1));
        assertEquals(Integer.valueOf(1), map.put(new String("one"), 2));
        assertEquals(Integer.valueOf(2), map.get("one"));
        assertEquals(1, map.size());
    }
    @Test
    public void testNullKeyAndValue() {
        final MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put(null, null);
        assertTrue(map.containsKey(null));
        assertNull(map.get(null));
        assertNull(map.put(null, 3));
        assertEquals(Integer.valueOf(3), map.remove(null));
        assertFalse(map.containsKey(null));
    }
    @Test
    public void testSameFullHashCollisionAndRemoval() {
        final MyHashMap<String, Integer> map = new MyHashMap<>();
        assertEquals("Aa".hashCode(), "BB".hashCode());
        map.put("Aa", 1);
        map.put("BB", 2);
        assertEquals(Integer.valueOf(1), map.get("Aa"));
        assertEquals(Integer.valueOf(2), map.get("BB"));
        assertEquals(Integer.valueOf(1), map.remove("Aa"));
        assertEquals(Integer.valueOf(2), map.get("BB"));
    }
    @Test
    public void testRemoveMiddleHeadTailAndAbsentCollision() {
        final MyHashMap<Integer, Integer> map = new MyHashMap<>();
        map.put(1, 1); map.put(17, 17); map.put(33, 33);
        assertNull(map.remove(49));
        assertEquals(Integer.valueOf(17), map.remove(17));
        assertEquals(Integer.valueOf(33), map.remove(33));
        assertEquals(Integer.valueOf(1), map.remove(1));
        assertEquals(0, map.size());
    }
    @Test
    public void testNegativeAndMinimumHash() {
        final MyHashMap<Integer, Integer> map = new MyHashMap<>();
        map.put(-1, 1); map.put(Integer.MIN_VALUE, 2);
        assertEquals(Integer.valueOf(1), map.get(-1));
        assertEquals(Integer.valueOf(2), map.get(Integer.MIN_VALUE));
    }
    @Test
    public void testManyResizesPreserveEntries() {
        final MyHashMap<Integer, Integer> map = new MyHashMap<>();
        for (int i = 0; i < 3000; i++) { map.put(i, i * 2); }
        for (int i = 0; i < 3000; i++) { assertEquals(Integer.valueOf(i * 2), map.get(i)); }
        assertEquals(3000, map.size());
    }
    @Test
    public void testDeterministicRandomParity() {
        final MyHashMap<Integer, Integer> actual = new MyHashMap<>();
        final Map<Integer, Integer> expected = new HashMap<>();
        final Random random = new Random(2026);
        for (int i = 0; i < 5000; i++) {
            final int id = random.nextInt(100);
            final Integer key = id == 0 ? null : id;
            final Integer value = random.nextInt(5) == 0 ? null : random.nextInt();
            switch (random.nextInt(3)) {
                case 0: assertEquals(expected.put(key, value), actual.put(key, value)); break;
                case 1: assertEquals(expected.remove(key), actual.remove(key)); break;
                default: assertEquals(expected.get(key), actual.get(key)); break;
            }
            assertEquals(expected.size(), actual.size());
            for (final Integer candidate : expected.keySet()) {
                assertTrue(actual.containsKey(candidate));
                assertEquals(expected.get(candidate), actual.get(candidate));
            }
        }
    }
}
