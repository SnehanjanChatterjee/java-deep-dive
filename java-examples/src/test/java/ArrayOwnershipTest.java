import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/** Checks row copies preserve shape without retaining mutable aliases. @author Snehanjan Chatterjee */
public final class ArrayOwnershipTest {
    @Test public void testDetachedRaggedAndNullRows() {
        final int[][] source = {{1}, null, {2, 3}};
        final int[][] copy = ArrayOwnership.copyRows(source);
        assertNotSame(source, copy); assertNotSame(source[0], copy[0]);
        assertNull(copy[1]); assertEquals(2, copy[2].length);
        source[0][0] = 9; copy[2][0] = 8;
        assertEquals(1, copy[0][0]); assertEquals(2, source[2][0]);
    }
    @Test public void testEmptyAndNullOuterArray() {
        assertEquals(0, ArrayOwnership.copyRows(new int[0][]).length);
        assertThrows(NullPointerException.class, () -> ArrayOwnership.copyRows(null));
    }
}
