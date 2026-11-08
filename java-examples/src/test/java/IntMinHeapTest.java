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
    @Test public void testBottomUpHeapifyAndInputCopy() {
        final int[] input = {9, 4, 7, 1, 2};
        final IntMinHeap heap = new IntMinHeap(input);
        input[0] = -100;
        for (final int value : new int[] {1, 2, 4, 7, 9}) { assertEquals(value, heap.poll()); }
        final IntMinHeap empty = new IntMinHeap(new int[0]);
        empty.offer(3); assertEquals(3, empty.poll());
        assertEquals(7, new IntMinHeap(new int[] {7}).poll());
        assertThrows(NullPointerException.class, () -> new IntMinHeap(null));
        assertThrows(IllegalArgumentException.class, () -> new IntMinHeap(new int[(1 << 20) + 1]));
    }
}
