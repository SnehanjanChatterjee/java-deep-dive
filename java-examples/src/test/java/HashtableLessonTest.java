import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Verifies the copied-key contract and the legacy API distinctions. @author Snehanjan Chatterjee */
public final class HashtableLessonTest {
    @Test public void testCopiesEveryKeyWithoutDependingOnOrder() {
        final Hashtable<String, Integer> table = new Hashtable<>();
        table.put("Ada", 20); table.put("Lin", 8);
        final List<String> result = HashtableLesson.copyKeys(table);
        assertEquals(2, result.size());
        assertTrue(result.contains("Ada")); assertTrue(result.contains("Lin"));
        table.put("Mira", 30);
        assertEquals(2, result.size());
        result.clear();
        assertEquals(3, table.size());
    }
    @Test public void testCopiesAnEmptyTable() {
        assertTrue(HashtableLesson.copyKeys(new Hashtable<String, Integer>()).isEmpty());
    }
    @Test public void testRejectsNullTable() {
        assertThrows(NullPointerException.class, () -> HashtableLesson.copyKeys(null));
    }
    @Test public void testContainsQueriesValuesRatherThanKeys() {
        final Hashtable<String, Integer> table = new Hashtable<>(); table.put("Ada", 20);
        assertTrue(table.containsKey("Ada")); assertFalse(table.contains("Ada"));
        assertTrue(table.contains(20)); assertTrue(table.containsValue(20));
        assertEquals(Integer.valueOf(20), table.put("Ada", 21));
        assertEquals(1, table.size());
    }
    @Test public void testRejectsNullKeyAndValueWithoutInstallingThem() {
        final Hashtable<String, Integer> table = new Hashtable<>(); table.put("Ada", 20);
        assertThrows(NullPointerException.class, () -> table.put(null, 1));
        assertThrows(NullPointerException.class, () -> table.put("Lin", null));
        assertEquals(1, table.size()); assertEquals(Integer.valueOf(20), table.get("Ada"));
        assertFalse(table.containsKey("Lin"));
    }
    @Test public void testEnumerationExhaustionAndUnequalCollisions() {
        final Hashtable<String, Integer> table = new Hashtable<>();
        final Enumeration<String> empty = table.keys();
        assertFalse(empty.hasMoreElements());
        assertThrows(NoSuchElementException.class, empty::nextElement);
        table.put("Aa", 1); table.put("BB", 2);
        assertEquals("Aa".hashCode(), "BB".hashCode());
        assertEquals(Integer.valueOf(1), table.get("Aa"));
        assertEquals(Integer.valueOf(2), table.get("BB"));
        assertEquals(2, HashtableLesson.copyKeys(table).size());
    }
}
