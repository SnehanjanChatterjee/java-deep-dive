import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

/** Exercises expiry, domination, duplicates and signed values. @author Snehanjan Chatterjee */
public final class CoreSlidingWindowTest {
    @Test public void testOverlappingAndBoundaryWindows() {
        assertArrayEquals(new int[] {3, 3, 5, 5, 6, 7}, CoreSlidingWindow.maxima(new int[] {1, 3, -1, -3, 5, 3, 6, 7}, 3));
        assertArrayEquals(new int[] {-1, -2, -3}, CoreSlidingWindow.maxima(new int[] {-1, -2, -3}, 1));
        assertArrayEquals(new int[] {7}, CoreSlidingWindow.maxima(new int[] {7, 7, 7}, 3));
        assertArrayEquals(new int[] {5, 4, 3}, CoreSlidingWindow.maxima(new int[] {5, 4, 3, 2}, 2));
    }
    @Test public void testInvalidInput() {
        assertThrows(NullPointerException.class, () -> CoreSlidingWindow.maxima(null, 1));
        assertThrows(IllegalArgumentException.class, () -> CoreSlidingWindow.maxima(new int[] {}, 1));
        assertThrows(IllegalArgumentException.class, () -> CoreSlidingWindow.maxima(new int[] {1}, 0));
        assertThrows(IllegalArgumentException.class, () -> CoreSlidingWindow.maxima(new int[] {1}, 2));
    }
}
