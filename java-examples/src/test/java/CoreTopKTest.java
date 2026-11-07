import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

/** Checks the selection boundary and duplicate retention. @author Snehanjan Chatterjee */
public final class CoreTopKTest {
    @Test public void testSelectionDuplicatesAndZero() {
        assertArrayEquals(new int[] {5, 6, 7}, CoreTopK.largest(new int[] {4, 1, 7, 5, 2, 6}, 3));
        assertArrayEquals(new int[] {7, 7}, CoreTopK.largest(new int[] {1, 7, 7, 3}, 2));
        assertArrayEquals(new int[] {-3, -1}, CoreTopK.largest(new int[] {-8, -1, -3}, 2));
        assertArrayEquals(new int[] {}, CoreTopK.largest(new int[] {}, 0));
        assertArrayEquals(new int[] {1, 2}, CoreTopK.largest(new int[] {2, 1}, 2));
    }
    @Test public void testRejectedRequests() {
        assertThrows(NullPointerException.class, () -> CoreTopK.largest(null, 0));
        assertThrows(IllegalArgumentException.class, () -> CoreTopK.largest(new int[] {1}, -1));
        assertThrows(IllegalArgumentException.class, () -> CoreTopK.largest(new int[] {1}, 2));
    }
}
