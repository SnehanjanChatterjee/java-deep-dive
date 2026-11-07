import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Checks map-backed sorted membership without claiming balancing. @author Snehanjan Chatterjee */
public final class SimpleTreeSetTest {
    @Test public void testMembershipTraversalAndRemoval() {
        final SimpleTreeSet<Integer> set = new SimpleTreeSet<>();
        assertEquals(Collections.emptyList(), set.asList());
        assertFalse(set.remove(20));
        assertTrue(set.add(30)); assertTrue(set.add(10)); assertTrue(set.add(20));
        assertFalse(set.add(10)); assertTrue(set.contains(20));
        assertEquals(Arrays.asList(10, 20, 30), set.asList());
        assertTrue(set.remove(20)); assertFalse(set.contains(20));
        assertEquals(2, set.size());
        set.asList().clear(); assertEquals(2, set.size());
    }
    @Test public void testComparisonEquivalenceAndNull() {
        final SimpleTreeSet<BigDecimal> set = new SimpleTreeSet<>();
        assertTrue(set.add(new BigDecimal("1.0")));
        assertFalse(set.add(new BigDecimal("1.00")));
        assertEquals(1, set.size());
        assertThrows(NullPointerException.class, () -> set.add(null));
        assertThrows(NullPointerException.class, () -> set.contains(null));
        assertThrows(NullPointerException.class, () -> set.remove(null));
    }
}
