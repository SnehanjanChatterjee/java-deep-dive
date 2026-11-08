import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

/** Checks dimensions, row identity and two-level copying. @author Snehanjan Chatterjee */
public final class NestedCollectionsTest {
    @Test public void testIndependentRowsAndJaggedMutation() {
        final List<List<Integer>> grid = NestedCollections.matrix(2, 2, 0);
        assertNotSame(grid.get(0), grid.get(1));
        grid.get(0).set(1, 7); grid.get(1).add(8);
        assertEquals(Arrays.asList(Arrays.asList(0, 7), Arrays.asList(0, 0, 8)), grid);
        assertEquals(Collections.emptyList(), NestedCollections.matrix(0, 3, 0));
        assertEquals(Arrays.asList(Collections.emptyList()), NestedCollections.matrix(1, 0, 0));
    }
    @Test public void testCopiesDetachEachRowAndPermitNullCells() {
        final List<List<Integer>> input = new ArrayList<>();
        input.add(new ArrayList<>(Arrays.asList(1, null)));
        final List<List<Integer>> copied = NestedCollections.copyRows(input);
        input.get(0).set(0, 9); input.add(new ArrayList<Integer>());
        assertEquals(Arrays.asList(Arrays.asList(1, null)), copied);
        copied.get(0).set(0, 5);
        assertEquals(Integer.valueOf(9), input.get(0).get(0));
    }
    @Test public void testInvalidDimensionsAndRows() {
        assertThrows(IllegalArgumentException.class, () -> NestedCollections.matrix(-1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> NestedCollections.matrix(0, -1, 0));
        assertThrows(NullPointerException.class, () -> NestedCollections.copyRows(null));
        assertThrows(NullPointerException.class, () -> NestedCollections.copyRows(Arrays.asList((List<Integer>) null)));
    }
    @Test public void testNestedSetsUseFrozenMembershipRatherThanEncounterOrder() {
        final Set<String> row = new LinkedHashSet<>(Arrays.asList("A", "B"));
        final Set<Set<String>> rows = NestedCollections.freezeDistinctRows(Arrays.asList(row,
                new LinkedHashSet<>(Arrays.asList("B", "A"))));
        assertEquals(1, rows.size()); row.add("C");
        assertEquals(Collections.singleton(new LinkedHashSet<>(Arrays.asList("A", "B"))), rows);
        assertThrows(UnsupportedOperationException.class, () -> rows.iterator().next().add("D"));
        assertThrows(UnsupportedOperationException.class, () -> rows.clear());
        assertThrows(NullPointerException.class, () -> NestedCollections.freezeDistinctRows(null));
        assertThrows(NullPointerException.class, () -> NestedCollections.freezeDistinctRows(Arrays.asList((Set<String>) null)));
    }
}
