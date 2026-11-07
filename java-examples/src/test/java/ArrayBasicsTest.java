import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks traversal, first-match semantics and exceptional inputs. @author Snehanjan Chatterjee */
public final class ArrayBasicsTest {
    @Test public void testSumAndFirstMatch() {
        assertEquals(0, ArrayBasics.sum(new int[0]));
        assertEquals(15, ArrayBasics.sum(new int[] {4, 7, 4}));
        assertEquals(0, ArrayBasics.firstIndexOf(new int[] {4, 7, 4}, 4));
        assertEquals(1, ArrayBasics.firstIndexOf(new int[] {4, 7, 4}, 7));
        assertEquals(-1, ArrayBasics.firstIndexOf(new int[0], 4));
        assertEquals(-1, ArrayBasics.firstIndexOf(new int[] {4}, 9));
    }
    @Test public void testNullAndOverflow() {
        assertThrows(NullPointerException.class, () -> ArrayBasics.sum(null));
        assertThrows(NullPointerException.class, () -> ArrayBasics.firstIndexOf(null, 1));
        assertThrows(ArithmeticException.class, () -> ArrayBasics.sum(new int[] {Integer.MAX_VALUE, 1}));
    }
    @Test public void testCountMatchesAndEmptyInput() {
        assertEquals(2, ArrayBasics.count(new int[] {4, 7, 4}, 4));
        assertEquals(0, ArrayBasics.count(new int[] {4}, 7));
        assertEquals(0, ArrayBasics.count(new int[0], 4));
        assertThrows(NullPointerException.class, () -> ArrayBasics.count(null, 4));
    }
}
