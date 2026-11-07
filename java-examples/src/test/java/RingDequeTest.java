import java.util.ArrayDeque;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/** Compares logical endpoint behavior across wrapping and growth. @author Snehanjan Chatterjee */
public final class RingDequeTest {
    @Test public void testMixedEndpointsMatchReference() {
        final RingDeque<Integer> actual = new RingDeque<>();
        final ArrayDeque<Integer> expected = new ArrayDeque<>();
        final Random random = new Random(42L);
        for (int i = 0; i < 1000; i++) {
            switch (random.nextInt(4)) {
                case 0: actual.addFirst(i); expected.addFirst(i); break;
                case 1: actual.addLast(i); expected.addLast(i); break;
                case 2: assertEquals(expected.pollFirst(), actual.pollFirst()); break;
                default: assertEquals(expected.pollLast(), actual.pollLast());
            }
            assertEquals(expected.size(), actual.size());
        }
        while (!expected.isEmpty()) { assertEquals(expected.pollFirst(), actual.pollFirst()); }
        assertNull(actual.pollLast()); assertNull(actual.pollFirst());
    }
    @Test public void testNullRejectionAndReuse() {
        final RingDeque<String> deque = new RingDeque<>();
        assertThrows(NullPointerException.class, () -> deque.addFirst(null));
        assertThrows(NullPointerException.class, () -> deque.addLast(null));
        assertEquals(0, deque.size());
        deque.addLast("A"); assertEquals("A", deque.pollLast());
        deque.addFirst("B"); assertEquals("B", deque.pollFirst()); assertEquals(0, deque.size());
    }
}
