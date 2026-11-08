import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks insertion-point decoding, duplicate membership and mutability boundaries. @author Snehanjan Chatterjee */
public final class CollectionAlgorithmsTest {
    @Test public void testSortedInsertionAtEveryBoundary() {
        final List<Integer> values = new ArrayList<>();
        CollectionAlgorithms.insertSorted(values, 2);
        CollectionAlgorithms.insertSorted(values, 1);
        CollectionAlgorithms.insertSorted(values, 4);
        CollectionAlgorithms.insertSorted(values, 3);
        CollectionAlgorithms.insertSorted(values, 2);
        assertEquals(Arrays.asList(1, 2, 2, 3, 4), values);
    }
    @Test public void testNullAndUnsupportedMutation() {
        assertThrows(NullPointerException.class, () -> CollectionAlgorithms.insertSorted(null, 1));
        assertThrows(UnsupportedOperationException.class,
                () -> CollectionAlgorithms.insertSorted(Collections.singletonList(1), 2));
    }
}
