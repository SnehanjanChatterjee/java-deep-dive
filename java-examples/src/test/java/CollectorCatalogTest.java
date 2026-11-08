import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks deliberate merge, grouping and finished-result ownership. @author Snehanjan Chatterjee */
public final class CollectorCatalogTest {
    @Test public void testGroupingAndDuplicatePointMerge() {
        final List<CollectorCatalog.Score> values = Arrays.asList(new CollectorCatalog.Score("Ada", "red", 100),
                new CollectorCatalog.Score("Bob", "blue", 250), new CollectorCatalog.Score("Ada", "red", 100));
        final Map<String, Integer> totals = CollectorCatalog.totalsByName(values);
        assertEquals(Integer.valueOf(200), totals.get("Ada")); assertEquals(Integer.valueOf(250), totals.get("Bob"));
        assertEquals(Arrays.asList("Ada", "Ada"), CollectorCatalog.namesByTeam(values).get("red"));
        assertEquals(Collections.emptyMap(), CollectorCatalog.totalsByName(Collections.<CollectorCatalog.Score>emptyList()));
        assertEquals(Collections.emptyMap(), CollectorCatalog.namesByTeam(Collections.<CollectorCatalog.Score>emptyList()));
    }
    @Test public void testUnmodifiableDetachedNameResult() {
        final List<CollectorCatalog.Score> values = new ArrayList<>(); values.add(new CollectorCatalog.Score("Ada", "red", 1));
        final List<String> names = CollectorCatalog.readOnlyNames(values);
        values.clear(); assertEquals(Collections.singletonList("Ada"), names);
        assertThrows(UnsupportedOperationException.class, () -> names.add("Bob"));
    }
    @Test public void testNullNegativeAndOverflowFailures() {
        assertThrows(NullPointerException.class, () -> new CollectorCatalog.Score(null, "red", 1));
        assertThrows(NullPointerException.class, () -> new CollectorCatalog.Score("Ada", null, 1));
        assertThrows(IllegalArgumentException.class, () -> new CollectorCatalog.Score("Ada", "red", -1));
        assertThrows(NullPointerException.class, () -> CollectorCatalog.totalsByName(null));
        assertThrows(NullPointerException.class, () -> CollectorCatalog.namesByTeam(null));
        assertThrows(NullPointerException.class, () -> CollectorCatalog.readOnlyNames(null));
        assertThrows(ArithmeticException.class, () -> CollectorCatalog.totalsByName(Arrays.asList(
                new CollectorCatalog.Score("Ada", "red", Integer.MAX_VALUE), new CollectorCatalog.Score("Ada", "red", 1))));
    }
    @Test public void testThreeFunctionMutableCollection() {
        assertEquals("AB", CollectorCatalog.concatenate(Arrays.asList("A", "B")));
        assertEquals("", CollectorCatalog.concatenate(Collections.<String>emptyList()));
        assertThrows(NullPointerException.class, () -> CollectorCatalog.concatenate(null));
        assertThrows(NullPointerException.class, () -> CollectorCatalog.concatenate(Arrays.asList("A", null)));
    }
}
