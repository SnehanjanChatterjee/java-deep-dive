import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/** Checks order, duplicates, signed values, growth and reuse. @author Snehanjan Chatterjee */
public final class IntMinHeapTest {
    @Test public void testSortedRemovalAcrossGrowth() {
        final int[] input = new Random(42L).ints(200, -1000, 1000).toArray();
        final IntMinHeap heap = new IntMinHeap();
        for (final int value : input) { heap.offer(value); }
        Arrays.sort(input);
        for (final int value : input) { assertEquals(value, heap.peek()); assertEquals(value, heap.poll()); }
        assertEquals(0, heap.size());
    }
    @Test public void testEmptySingleDuplicateAndExtremeValues() {
        final IntMinHeap heap = new IntMinHeap();
        assertThrows(NoSuchElementException.class, heap::peek);
        assertThrows(NoSuchElementException.class, heap::poll);
        heap.offer(1); assertEquals(1, heap.poll());
        heap.offer(Integer.MAX_VALUE); heap.offer(0); heap.offer(Integer.MIN_VALUE); heap.offer(0);
        assertEquals(Integer.MIN_VALUE, heap.poll()); assertEquals(0, heap.poll());
        assertEquals(0, heap.poll()); assertEquals(Integer.MAX_VALUE, heap.poll());
    }
}
