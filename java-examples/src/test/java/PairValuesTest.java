import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/** Checks composite value equality without treating diagnostics as an encoding. @author Snehanjan Chatterjee */
public final class PairValuesTest {
    @Test public void testValueEqualityHashAndOrder() {
        final PairValues.Pair<Integer, Integer> pair = new PairValues.Pair<>(3, 5);
        final PairValues.Pair<Integer, Integer> equal = new PairValues.Pair<>(3, 5);
        assertEquals(pair, pair); assertEquals(pair, equal); assertEquals(pair.hashCode(), equal.hashCode());
        assertNotEquals(pair, new PairValues.Pair<>(5, 3));
        assertFalse(pair.equals(null)); assertFalse(pair.equals("(3, 5)"));
        assertEquals(Integer.valueOf(3), pair.first()); assertEquals(Integer.valueOf(5), pair.second());
        assertEquals("(3, 5)", pair.toString());
        final Set<PairValues.Pair<Integer, Integer>> values = new HashSet<>();
        assertTrue(values.add(pair)); assertFalse(values.add(equal));
    }
    @Test public void testNullComponentsAndAmbiguousText() {
        final PairValues.Pair<String, String> empty = new PairValues.Pair<>(null, null);
        assertEquals(empty, new PairValues.Pair<String, String>(null, null));
        assertNull(empty.first()); assertNull(empty.second());
        assertNotEquals(empty, new PairValues.Pair<>(null, "x"));
        assertNotEquals(new PairValues.Pair<>("a,b", "c"), new PairValues.Pair<>("a", "b,c"));
    }
    @Test public void testFinalReferencesDoNotCopyMutableComponents() {
        final List<Integer> component = new ArrayList<>(Arrays.asList(1));
        final PairValues.Pair<List<Integer>, String> pair = new PairValues.Pair<>(component, "label");
        assertSame(component, pair.first());
        component.add(2);
        assertEquals(Arrays.asList(1, 2), pair.first());
    }
}
